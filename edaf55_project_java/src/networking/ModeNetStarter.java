package networking;
import client.ClientMonitor;

public class ModeNetStarter {

	private NetMonitor netMon;
	private Thread[] threads;
	
	public ModeNetStarter(ClientMonitor cMon, String ip, int port) {

		netMon = new NetMonitor();
		threads = new Thread[] {
			new ModeSender(netMon, cMon),
			new ClientConnectionThread(netMon, ip, port),
			new ClientShutdownThread(netMon)
		};
	}
	
	public void startMe() {
		System.out.println("Starting SendMode");
		
		// Start threads
		for (Thread thread : threads) thread.start();
	}
}
