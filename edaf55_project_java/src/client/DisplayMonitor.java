package client;

public class DisplayMonitor {

	private Picture pic_1, pic_2;
	private SwingGui gui;
	private long showedTime = 0;
	private long lastTimeStamp;
	private long timeDiff;
	private boolean isSynced = false;

	public DisplayMonitor(SwingGui gui) {
		this.gui = gui;
	}

	public synchronized void updatePicture1(Picture pic) {
		System.out.println("Image updated in displaymonitor");
		this.pic_1 = pic;
		syncDelayThread(pic);
		gui.updateImage1(pic_1);
		showedTime = System.currentTimeMillis();
		lastTimeStamp = pic.timeStamp;
	}

	public synchronized void updatePicture2(Picture pic) {
		this.pic_2 = pic;
		syncDelayThread(pic);
		gui.updateImage1(pic_2);
		showedTime = System.currentTimeMillis();
		lastTimeStamp = pic.timeStamp;
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
				//e.printStackTrace();
			} //MEGA DANGER
		}
	}

	public void setSynced(boolean syncMode) {
		isSynced = syncMode;
	}

}