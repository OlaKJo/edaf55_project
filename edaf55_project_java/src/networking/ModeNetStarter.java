package networking;
import client.ClientMonitor;

public class ModeNetStarter {

	private ClientMonitor cMon;
	private String ip;
	private int port;
	private NetMonitor netMon;
	private Thread[] threads;
	
	public ModeNetStarter(ClientMonitor cMon, String ip, int port) {
		this.cMon = cMon;
		this.ip = ip;
		this.port = port;
		
		netMon = new NetMonitor();
		threads = new Thread[] {
			new SendMode(netMon, cMon),
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
