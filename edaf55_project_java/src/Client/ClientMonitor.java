package Client;

public class ClientMonitor {

	private boolean sync;
	private int[] picBuffer1, picBuffer2;
	private int delayedFrames;
	private long prevFrameTime;

	private SendMode sendModeCam1, sendModeCam2;
	private Display displayCam1, displayCam2;

	// Number of delayed frames tolerated before mode switch
	private final int delayedFramesTolerance = 10;

	public ClientMonitor(Display displayCam1, Display displayCam2) {

		sendModeCam1 = new SendMode();
		sendModeCam2 = new SendMode();

		this.displayCam1 = displayCam1;
		this.displayCam2 = displayCam2;

		delayedFrames = 0;
		this.sync = false;

	}

	public synchronized void setModeSync(boolean sync) {

		if (this.sync == false && sync== true)
			delayedFrames = -1;// Ignore first frame delay
		this.sync = sync;
		
		sendModeCam1.send(sync);
		sendModeCam2.send(sync);

	}

	public void putPicture1(int[] pic) {
		picBuffer1 = pic;
		displayCam1.putImage(pic);
		notifyAll();
		if (sync)
			pictureDelayCheck();
	}
	
	public void putPicture2(int[] pic) {
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
				setModeSync(false);
		} else {
			delayedFrames = 0;
		}
	}

}
