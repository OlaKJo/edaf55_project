package client;

public class SendMode extends Thread {
		
	private ClientMonitor monitor;
	
	public SendMode(ClientMonitor monitor) {
		this.monitor = monitor;
	}
	
	public void run(){
		while(true) {
			monitor.getModeUpdate();
			//Send mode
		}
	}
	
}
