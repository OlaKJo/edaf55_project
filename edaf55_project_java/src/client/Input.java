package client;

public class Input extends Thread {

	private ClientMonitor monitor;
	private SwingGui gui;
	
	private Input(ClientMonitor monitor, SwingGui gui) {
		this.monitor = monitor;
		this.gui = gui;
	}
	
	public void run(){
		while(true) {
			monitor.setMode(gui.getInput());
		}
	}
	
}
