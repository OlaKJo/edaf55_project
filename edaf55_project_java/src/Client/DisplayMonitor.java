package Client;

public class DisplayMonitor {

	private int[] pic_1, pic_2;
	// flags for pics received and available to fetch
	private boolean pic1Available, pic2Available;

	public DisplayMonitor() {
		pic1Available = false;
		pic2Available = false;
	}

	public synchronized void updatePicture1(int[] pic) {
		this.pic_1 = pic;
		pic1Available = true;
		notifyAll();
	}

	public synchronized void updatePicture2(int[] pic) {
		this.pic_2 = pic;
		pic2Available = true;
		notifyAll();
	}

	public synchronized int[] getPic1() {

		try {
			while (!pic1Available) wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		pic1Available = false;
		return pic_1;
	}

	public synchronized int[] getPic2() {

		try {
			while (!pic2Available) wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		pic2Available = false;
		return pic_2;
	}

}
