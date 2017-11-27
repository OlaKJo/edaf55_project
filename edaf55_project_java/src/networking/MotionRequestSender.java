package networking;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import client.ClientMonitor;

public class MotionRequestSender extends Thread {

	private NetMonitor monitor;
	private byte[] buffer;
	private ClientMonitor cMon;
	
	public MotionRequestSender(NetMonitor netMon, ClientMonitor cMon) {
		monitor = netMon;
		this.cMon = cMon;
		buffer = new byte[6];
	}
	
	public void run() {
		while (!monitor.isShutdown())
		{
			try {
				// Blocking wait for connection
				System.out.println("waiting for connection to motion socket");
				monitor.waitUntilActive();
				System.out.println("received connection to motion socket");

				Socket socket = monitor.getSocket();
				OutputStream os = socket.getOutputStream();
				
				// Send data packages of different sizes
				while (true) {
					//boolean mode = cMon.getModeUpdate();
					MotionPack.pack(buffer);
					//Utils.printBuffer("ClientWriteThread", size, buffer);

					// Send package
					os.write(buffer, 0, 6);
					
					// Flush data
					os.flush();
					Thread.sleep(5000);
					System.out.println("requested motion data");
				}
			} catch (IOException e) {
				// Something happened with the connection
				//
				// Occurs if there is an error trying to write data,
				// for instance that the connection suddenly closed.
				monitor.setActive(false);
				System.out.println("No connection on client side");
			} catch (InterruptedException e) {
				// Occurs when interrupted
				monitor.shutdown();
				break;
			}
		}
		
		System.out.println("Exiting SendMotionRequest");
	}
}
