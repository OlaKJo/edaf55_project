package client;

public class ClientMonitor {

	private int mode;
	private boolean sync;
	private byte[] picBuffer1, picBuffer2;
	private int delayedFrames;
	private long timeStampPic1, timeStampPic2;
	private boolean modeChanged;
	private boolean pic1Available, pic2Available;

	// Number of delayed frames tolerated before mode switch
	private final int delayedFramesTolerance = 10;
	private final long syncToleranceMillis = 200;
	public static final int MODE_AUTO = 0, MODE_IDLE = 1, MODE_MOVIE = 2, MODE_SYNC = 4, MODE_ASYNC = 5;

	public ClientMonitor() {

		delayedFrames = 0;
		this.sync = false;
		mode = MODE_AUTO;
		modeChanged = false;
		pic1Available = false;
		pic2Available = false;

	}

	public synchronized void setMode(int mode) {

		this.mode = mode;
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
		System.out.println("Image put in clientMonitor");
		if (camNumber == 1) {
			picBuffer1 = pic;
			timeStampPic1 = timeStamp;
			pic1Available = true;
		} else {
			picBuffer2 = pic;
			timeStampPic2 = timeStamp;
			pic2Available = true;
		}
		notifyAll();
		if (mode == MODE_AUTO && sync == false) {
			syncCheck();
		}
		else if(mode == MODE_AUTO && sync == false){
			asyncCheck();
		}
	}

	public synchronized byte[] getPicture(int camNumber) {
		if (camNumber == 1) {
			try {
				while (!pic1Available)
					wait();
				System.out.println("Image returned from ClientMonitor");
				pic1Available = false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return picBuffer1;
		} else {
			try {
				while (!pic2Available)
					wait();
				pic2Available = false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return picBuffer2;
		}
	}

	// Check sync to async conditions
	private void syncCheck() {
		long currentTimeStamp = System.currentTimeMillis();
		if ((timeStampPic1 - currentTimeStamp)
				- (timeStampPic2 -currentTimeStamp) > syncToleranceMillis) {
			delayedFrames++;
			// Delayed frames, enter asynchronous mode
			if (delayedFrames > delayedFramesTolerance) {
				mode = MODE_IDLE;
				modeChanged = true;
				notifyAll();
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
			// Delay stabilized, resume synchrous operation
			if (delayedFrames < delayedFramesTolerance) {
				mode = MODE_MOVIE;
				modeChanged = true;
				notifyAll();
				delayedFrames = 10;
			}
		} else {
			delayedFrames = 10;
		}
	}

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

}
