package client;

public class Display extends Thread {
	
	private ClientMonitor clientMonitor;
	private DisplayMonitor displayMonitor;
	private int CAM_NBR;
	
	public Display(DisplayMonitor displayMonitor, ClientMonitor clientMonitor, int CAM_NBR) {
		this.displayMonitor = displayMonitor;
		this.clientMonitor = clientMonitor;
		this.CAM_NBR = CAM_NBR;
	}
	
	public void run() {
		while(true) {
			byte[] pic = clientMonitor.getPicture(CAM_NBR);
			if(CAM_NBR == 1) {
				displayMonitor.updatePicture1(pic);
			}
			else {
				displayMonitor.updatePicture2(pic);
			}
		}
	}
	
}
