package Client;

public class DisplayMonitor {

	private byte[] pic_1, pic_2;
	private SwingGui gui;
	private long timeStampPic1, timeStampPic2;

	public DisplayMonitor(SwingGui gui) {
		this.gui = gui;
		timeStampPic1 = System.currentTimeMillis();
		timeStampPic2 = System.currentTimeMillis();
	}

	public synchronized void updatePicture1(byte[] pic) {
		long arrivalTime = System.currentTimeMillis();
		this.pic_1 = pic;
		gui.updateImage1(pic_1, (int)(arrivalTime - timeStampPic1));
		timeStampPic1 = arrivalTime;
	}

	public synchronized void updatePicture2(byte[] pic) {
		long arrivalTime = System.currentTimeMillis();
		this.pic_2 = pic;
		gui.updateImage1(pic_2, (int)(arrivalTime - timeStampPic2));
		timeStampPic2 = arrivalTime;
	}

}
