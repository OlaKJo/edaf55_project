package client;

import java.util.LinkedList;

public class DisplayMonitor {

	private Picture pic_1, pic_2;
	private SwingGui gui;
	private long showedTime = 0;
	private long lastTimeStamp;
	private long timeDiff;
	private long currentAverageDelay = 0;
	private boolean isSynced = false;
	private LinkedList<Long> rollingTimestampAverage1;
	private LinkedList<Long> rollingTimestampAverage2;
	private int mode;
	private long timeOfStart = -1;

	public DisplayMonitor(SwingGui gui) {
		this.gui = gui;
		rollingTimestampAverage1 = new LinkedList<Long>();
		rollingTimestampAverage2 = new LinkedList<Long>();
	}

	public synchronized void updatePicture1(Picture pic) {
		System.out.println("Image updated in displaymonitor");
		this.pic_1 = pic;
		syncDelayThread(pic);
		gui.updateImage1(pic_1);
		showedTime = System.currentTimeMillis();
		lastTimeStamp = pic.timeStamp;

		rollingTimestampAverage1.add(System.currentTimeMillis() - pic.timeStamp);
		gui.setDelayLabel1(checkDelay(rollingTimestampAverage1));
		System.out.println("Current Time=" + (System.currentTimeMillis()) + "\nTime Stamp: " + pic.timeStamp);
	}

	private long checkDelay(LinkedList<Long> rollingTimestampAverage) {

		long delay;

		if (timeOfStart == -1) {
			timeOfStart = System.currentTimeMillis();
		}

		if (mode == ClientMonitor.MODE_IDLE) {
			delay = rollingTimestampAverage.getLast();
			rollingTimestampAverage.clear();
		} else {
			
			if (System.currentTimeMillis() - timeOfStart >= 2000) {
				currentAverageDelay = 0;
				timeOfStart = -1;
				for (long l : rollingTimestampAverage) {
					currentAverageDelay += l / rollingTimestampAverage.size();
				}
			}

			delay = currentAverageDelay;
		}
		
		if (rollingTimestampAverage.size() >= 180) {
			rollingTimestampAverage.removeFirst();
		}

		return delay;
	}

	public synchronized void updatePicture2(Picture pic) {
		this.pic_2 = pic;
		syncDelayThread(pic);
		gui.updateImage2(pic_2);
		showedTime = System.currentTimeMillis();
		lastTimeStamp = pic.timeStamp;

		rollingTimestampAverage2.add(System.currentTimeMillis() - pic.timeStamp);
		gui.setDelayLabel2(checkDelay(rollingTimestampAverage2));
	}

	public synchronized void setSyncLabel(boolean synced) {
		gui.setSyncLabel(synced);
	}

	private void syncDelayThread(Picture pic) {
		if (isSynced && !(showedTime == 0)) {
			timeDiff = pic.timeStamp - lastTimeStamp;
			try {
				wait(System.currentTimeMillis() - (showedTime + timeDiff));
			} catch (Exception e) { // DANGER DANGER
				// e.printStackTrace();
			} // MEGA DANGER
		}
	}

	public void setSynced(boolean syncMode) {
		isSynced = syncMode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

}