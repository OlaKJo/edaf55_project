package networking;
import client.ClientMonitor;

public class MotionNetStarter {

	private NetMonitor netMon;
	private Thread[] threads;
	
	public MotionNetStarter(ClientMonitor cMon, String ip, int port, int camNum) {

		netMon = new NetMonitor();
		threads = new Thread[] {
			new MotionRequestSender(netMon, cMon),
			new MotionDataReceiver(netMon, cMon, camNum),
			new ClientConnectionThread(netMon, ip, port, "MotionSender " + camNum),
			new ClientShutdownThread(netMon)
		};
	}
	
	public void startMe() {
		System.out.println("Starting SendMode");
		
		// Start threads
		for (Thread thread : threads) thread.start();
	}
}
