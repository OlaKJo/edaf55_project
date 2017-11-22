package client;

public class Input extends Thread {

	private ClientMonitor monitor;
	private SwingGui gui;

	public Input(ClientMonitor monitor, SwingGui gui) {
		this.monitor = monitor;
		this.gui = gui;
	}

	public void run() {
		while (true) {
			// gets {mode, synced}
			int[] inputs = gui.getInput();
			monitor.setMode(inputs[0]);
			if (inputs[1] == ClientMonitor.MODE_SYNC)
				monitor.setSync(true);
		}
	}

}
