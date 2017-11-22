package client;

public class Display extends Thread {
	
	private ClientMonitor clientMonitor;
	private DisplayMonitor displayMonitor;
	private int camNbr;
	
	public Display(DisplayMonitor displayMonitor, ClientMonitor clientMonitor, int CAM_NBR) {
		this.displayMonitor = displayMonitor;
		this.clientMonitor = clientMonitor;
		this.camNbr = CAM_NBR;
	}
	
	public void run() {
		while(true) {
			byte[] pic = clientMonitor.getPicture(camNbr);
			if(camNbr == 1) {
				displayMonitor.updatePicture1(pic);
			}
			else {
				displayMonitor.updatePicture2(pic);
			}
		}
	}
	
}
