package Client;

public class ClientMonitor {

	private int mode;
	private boolean sync;
	private byte[] picBuffer1, picBuffer2;
	private int delayedFrames;
	private long prevFrameTime;

	private SendMode sendModeCam1, sendModeCam2;
	private Display displayCam1, displayCam2;

	// Number of delayed frames tolerated before mode switch
	private final int delayedFramesTolerance = 10;
	private final int MODE_AUTO = 0, MODE_IDLE = 1, MODE_MOVIE = 2;

	public ClientMonitor(Display displayCam1, Display displayCam2) {

		sendModeCam1 = new SendMode();
		sendModeCam2 = new SendMode();

		this.displayCam1 = displayCam1;
		this.displayCam2 = displayCam2;

		delayedFrames = 0;
		this.sync = false;
		mode = MODE_AUTO;

	}
	
	public synchronized void setMode(int mode) {
		this.mode = mode;
	}

	public synchronized void setSync(boolean sync) {

		if (mode == MODE_AUTO) {
			if (this.sync == false && sync == true)
				delayedFrames = -1;// Ignore first frame delay
			this.sync = sync;

			sendModeCam1.send(sync);
			sendModeCam2.send(sync);
		}

	}

	public synchronized void putPicture1(byte[] pic) {
		picBuffer1 = pic;
		displayCam1.putImage(pic);
		notifyAll();
		if (sync)
			pictureDelayCheck();
	}

	public synchronized void putPicture2(byte[] pic) {
		picBuffer2 = pic;
		displayCam2.putImage(pic);
		notifyAll();
		if (sync)
			pictureDelayCheck();
	}

	private void pictureDelayCheck() {
		prevFrameTime += 200;
		if ((System.currentTimeMillis() - prevFrameTime) > 200) {
			delayedFrames++;
			if (delayedFrames > delayedFramesTolerance)
				setSync(false);
		} else {
			delayedFrames = 0;
		}
	}

}
