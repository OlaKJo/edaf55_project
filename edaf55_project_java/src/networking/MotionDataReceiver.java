package networking;

import java.io.IOException;
import java.io.InputStream;

import client.ClientMonitor;

public class MotionDataReceiver extends Thread {

	private NetMonitor netMonitor;
	private ClientMonitor cMon;
	private byte[] buffer;
	private int lastMotionTime;
	private int camNbr;

	public MotionDataReceiver(NetMonitor netMon, ClientMonitor cMon, int camNbr) {
		netMonitor = netMon;
		this.cMon = cMon;
		buffer = new byte[100];
		this.camNbr = camNbr;
	}

	public void run() {
		boolean running = true;
		while (!netMonitor.isShutdown()) {
			try {
				// Wait for active connection
				netMonitor.waitUntilActive();

				InputStream is = netMonitor.getSocket().getInputStream();

				// Receive data packages of different sizes
				while (running) {

					// Read header
					int size = MotionPack.HEAD_SIZE;
					int n = 0;
					while ((n = is.read(buffer, n, size)) > 0) {
						size -= n;
					}
					if (size != 0) {
						break;
					}
					
//					for (int i = 0; i < MotionPack.HEAD_SIZE; i++) {
//						System.out.print((char) buffer[i]);
//					}
					
					int timeSize = MotionPack.getTimeSize(buffer);
//					
					size = timeSize;
					n = MotionPack.HEAD_SIZE;
					while ((n = is.read(buffer, n, size)) > 0) {
						size -= n;
					}
					if (size != 0) {
						break;
					}

					int[] times = MotionPack.getTimes(buffer);

					int diff = times[0];
					int local = times[1];
					int remote = times[2];

//					System.out.println("diff: " + diff);
//					System.out.println("local: " + local);
//					System.out.println("remote: " + remote);
					
					boolean detected = false;
					if(local > lastMotionTime) {
						detected = true;
					}else{
						detected = false;
					}
					cMon.setMotionDetected(camNbr, detected);
					
					
				}

			} catch (IOException e) {
				// Something happened with the connection
				//
				// Example: the connection is closed on the server side, but
				// the client is still trying to write data.
				netMonitor.setActive(false);
				System.out.println("Failed to read data from server");
			} catch (InterruptedException e) {
				// Occurs when interrupted
				netMonitor.shutdown();
				break;
			}
		}

		// No resources to dispose since this is the responsibility
		// of the shutdown thread.
		System.out.println("Exiting MotionDataReceiver");
	}
}
