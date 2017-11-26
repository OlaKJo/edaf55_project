package client;

public class Input extends Thread {

	private ClientMonitor monitor;
	private SwingGui gui;
	private int oldSyncMode, oldMode;

	public Input(ClientMonitor monitor, SwingGui gui) {
		this.monitor = monitor;
		this.gui = gui;
		oldSyncMode = SwingGui.MODE_ASYNC;
		oldMode = ClientMonitor.MODE_AUTO;
	}

	public void run() {
		while (true) {
			// gets {mode, synced}
			int[] inputs = gui.getInput();
			if(oldMode != inputs[0]) {
				monitor.setMode(inputs[0]);
				oldMode = inputs[0];
			}
			if (oldSyncMode != inputs[1]) {
				monitor.setSync((inputs[1] == SwingGui.MODE_SYNC) ? true : false);
				oldSyncMode = inputs[1];
			}
		}
	}

}
