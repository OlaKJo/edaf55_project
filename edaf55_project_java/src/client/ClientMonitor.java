package client;

public class ClientMonitor {

	private int mode;
	private boolean sync;
	private byte[] picBuffer1, picBuffer2;
	private int delayedFrames;
	private long timeStampPic1, timeStampPic2;
	private boolean modeChanged;

	private Display displayCam1, displayCam2;

	// Number of delayed frames tolerated before mode switch
	private final int delayedFramesTolerance = 10;
	private final long syncToleranceMillis = 200;
	public static final int MODE_AUTO = 0, MODE_IDLE = 1, MODE_MOVIE = 2, MODE_SYNC = 4, MODE_ASYNC = 5;

	public ClientMonitor(Display displayCam1, Display displayCam2) {

		this.displayCam1 = displayCam1;
		this.displayCam2 = displayCam2;

		delayedFrames = 0;
		this.sync = false;
		mode = MODE_AUTO;
		modeChanged = false;

	}

	public synchronized void setMode(int mode) {
		if (mode == MODE_SYNC) {
			setSync(true);
		} else if (mode == MODE_ASYNC) {
			setSync(false);
		} else {
			this.mode = mode;
		}
		modeChanged = true;
		notifyAll();
	}

	public synchronized void setSync(boolean sync) {

		if (this.sync == false && sync == true)
			delayedFrames = -1;// Ignore first frame delay
		this.sync = sync;

		modeChanged = true;
		notifyAll();

	}

	public synchronized void putPicture(byte[] pic, long timeStamp, int camNumber) {
		if (camNumber == 1) {
			picBuffer1 = pic;
			displayCam1.putImage(pic);
			timeStampPic1 = timeStamp;
		} else {
			picBuffer2 = pic;
			displayCam2.putImage(pic);
			timeStampPic2 = timeStamp;
		}
		notifyAll();
		if (sync)
			syncCheck();
	}

	// Check sync to async conditions
	private void syncCheck() {
		if ((timeStampPic1 - System.currentTimeMillis())
				- (timeStampPic2 - System.currentTimeMillis()) > syncToleranceMillis) {
			delayedFrames++;
			//Delayed frames, enter asynchronous mode
			if (delayedFrames > delayedFramesTolerance) {
				setSync(false);
				delayedFrames = 10;
			}
		} else {
			delayedFrames = 0;
		}
	}

	// Check async to sync conditions
	private void asyncCheck() {
		if ((timeStampPic1 - System.currentTimeMillis())
				- (timeStampPic2 - System.currentTimeMillis()) < syncToleranceMillis) {
			delayedFrames--;
			//Delay stabilized, resume synchrous operation
			if (delayedFrames < delayedFramesTolerance) {
				setSync(false);
				delayedFrames = 10;
			}
		} else {
			delayedFrames = 10;
		}
	}

	public synchronized int getModeUpdate() {
		try {
			while (!modeChanged)
				wait();
			modeChanged = false;
		} catch (InterruptedException e) {
		}
		return mode;
	}

}
