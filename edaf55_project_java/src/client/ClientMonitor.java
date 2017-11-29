package client;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

public class ClientMonitor {

	private int mode;
	private int modeToSend;
	private boolean forceSync, synced;
	private LinkedList<Picture> picBuffer1, picBuffer2;
	private int delayedFrames;
	private long timeStampPic1, timeStampPic2;
	private boolean modeChanged;
	private boolean pic1Available, pic2Available;
	private boolean motionDetected1, motionDetected2;

	// Number of delayed frames tolerated before mode switch
	private final int delayedFramesTolerance = 10;
	private final long syncToleranceMillis = 200;
	public static final int MODE_AUTO = 0, MODE_IDLE = 1, MODE_MOVIE = 2;
	private boolean modeIsSetToAuto;
	private Picture lastPic1 = new Picture(null, 0);
	private Picture lastPic2 = new Picture(null, 0);
	private LinkedList<Long> syncStamps1, syncStamps2;
	private long baseline1, baseline2;

	public ClientMonitor() {

		delayedFrames = 0;
		this.forceSync = false;
		setSynced(false);
		setMode(MODE_AUTO);
		modeIsSetToAuto = true;
		modeChanged = false;
		pic1Available = false;
		pic2Available = false;
		picBuffer1 = new LinkedList<Picture>();
		picBuffer2 = new LinkedList<Picture>();
		syncStamps1 = new LinkedList<Long>();
		syncStamps2 = new LinkedList<Long>();

	}

	public synchronized void setMode(int mode) {
		if (mode == MODE_AUTO) {
			modeIsSetToAuto = true;
		} else {
			this.mode = mode;
			modeChanged = true;
			modeToSend = mode - 1;
		}
		notifyAll();
	}

	public synchronized void setForcedSync(boolean sync) {

		if (sync) {
			this.forceSync = true;
			System.out.println("Sync set to forced");
		}
	}
	
	private void setSynced(boolean sync) {
		synced = sync;
		System.out.println("synced set");
	}

	public synchronized void putPicture(Picture pic, int camNumber) {
		if (baseline1 == 0 && camNumber == 1) {
			setBaseLine(pic, 1);
		} else if(baseline2 == 0  && camNumber == 2) {
			setBaseLine(pic, 2);
		}
		
		pic.convertToJavaTime(camNumber == 1 ? baseline1 : baseline2); 
		
		System.out.println("Image put in clientMonitor");

		if (camNumber == 1) {
			picBuffer1.add(pic);
			lastPic1 = pic;
			pic1Available = true;
			if (lastPic2.timeStamp != 0)
				syncReqCheckCam1Add(pic);
		} else {
			picBuffer2.add(pic);
			lastPic2 = pic;
			pic2Available = true;
			if (lastPic1.timeStamp != 0)
				syncReqCheckCam2Add(pic);
		}
		notifyAll();
	}

	private void setBaseLine(Picture pic, int camNbr) {
		if (camNbr == 1) {
			baseline1 = System.currentTimeMillis()-pic.timeStamp;
		} else if (camNbr == 2) {
			baseline2 = System.currentTimeMillis()-pic.timeStamp;
		}
	}

	private void syncReqCheckCam1Add(Picture pic) {
		syncReqCheck(pic, lastPic2, syncStamps1, syncStamps2);
	}

	private void syncReqCheckCam2Add(Picture pic) {
		syncReqCheck(pic, lastPic1, syncStamps2, syncStamps1);
	}

	private void syncReqCheck(Picture pic, Picture otherLastPic, LinkedList<Long> syncStamps,
			LinkedList<Long> OtherSyncStamps) {

		long diff = 0;
		diff = pic.timeStamp - otherLastPic.timeStamp;
		boolean syncReq = (Math.abs(diff) <= syncToleranceMillis);

		if (synced)
			asyncCheck(syncReq, diff, syncStamps, OtherSyncStamps, pic, otherLastPic);
		else if (syncCheck(syncReq, syncStamps)) {
			syncStamps.add(pic.timeStamp);
		}
	}

	private void asyncCheck(boolean syncReq, long diff, LinkedList<Long> syncStamps, LinkedList<Long> OtherSyncStamps,
			Picture pic, Picture otherLastPic) {
		if (syncReq || diff < 0 || forceSync) {
			syncStamps1.clear();
			syncStamps2.clear();
			return;
		} else {
			syncStamps.add(pic.timeStamp);
			while ((syncStamps.peekFirst() - otherLastPic.timeStamp) <= syncToleranceMillis) {
				syncStamps.removeFirst();
			}
		}

		if (syncStamps1.size() >= delayedFramesTolerance || syncStamps2.size() >= delayedFramesTolerance) {
			syncStamps1.clear();
			syncStamps2.clear();
			setSynced(false);
		}
	}

	public synchronized Picture getPicture(int camNumber) {
		if (camNumber == 1) {
			try {
				while (!pic1Available)
					wait();
				System.out.println("Image returned from ClientMonitor");
				pic1Available = false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return picBuffer1.remove();
		} else {
			try {
				while (!pic2Available)
					wait();
				pic2Available = false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return picBuffer2.remove();
		}
	}

	private boolean syncCheck(boolean syncReq, LinkedList<Long> syncStamps) {
		if (forceSync || syncReq) {
			syncStamps1.clear();
			syncStamps2.clear();
			setSynced(true);
			return true;
		}
		return false;
	}

	// TODO
	public synchronized boolean getModeUpdate() {
		try {
			while (!modeChanged)
				wait();
			modeChanged = false;

		} catch (InterruptedException e) {
		}
		if (mode == MODE_MOVIE) {
			return true;
		} else {
			return false;
		}
	}

	public boolean getSyncMode() {
		return synced;
	}

	public synchronized void setMotionDetected(int camNbr, boolean detected) {
		if (camNbr == 1) {
			motionDetected1 = detected;
		} else {
			motionDetected2 = detected;
		}

		if (mode == MODE_AUTO) {
			if (motionDetected1 || motionDetected2) {
				modeToSend = 1;
			} else {
				modeToSend = 0;
			}
		}

		notifyAll();
	}

	public synchronized int getNewMode(int currentMode) {
		while (currentMode == modeToSend)
			try {
				// System.out.println("Checking getNewMode");
				// System.out.println("currentMode: " + currentMode);
				// System.out.println("modeToSend: " + modeToSend);
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		return modeToSend;

	}
	
	public int getMode(){
		return mode;
	}

}
