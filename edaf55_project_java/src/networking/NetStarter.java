package networking;

import Client.PictureReciever;

public class NetStarter {

	private static class StartClient extends Thread {
		public void run() {
			try {
				System.out.println("Starting network client");
				NetMonitor monitor = new NetMonitor();
				Thread[] threads = new Thread[] {
					new PictureReciever(monitor),
					new ClientConnectionThread(monitor, "10.2.228.211", 22222),
					new ClientShutdownThread(monitor)
				};
				
				// Start threads
				for (Thread thread : threads) thread.start();

				// Interrupt threads after some time
				Thread.sleep(10000);
				System.out.println("Interrupting client threads");
				for (Thread thread : threads) thread.interrupt(); // Interrupt threads
				for (Thread thread : threads) thread.join(); // Wait for threads to die

				System.out.println("Network client finished");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		try {
			Thread b = new StartClient();
			b.start();
			b.join();
		} catch (InterruptedException e) {
		}
	}
}
