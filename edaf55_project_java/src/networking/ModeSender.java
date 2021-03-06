package networking;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import client.ClientMonitor;


public class ModeSender extends Thread {

	private NetMonitor monitor;
	private byte[] buffer;
	private ClientMonitor cMon;
	private int mode;
	
	public ModeSender(NetMonitor mon, ClientMonitor cMon) {
		monitor = mon;
		this.cMon = cMon;
		buffer = new byte[1];
	}
	
	// Receive packages of random size from active connections.
	public void run() {
		while (!monitor.isShutdown())
		{
			try {
				// Blocking wait for connection
				System.out.println("waiting for connection to mode socket");
				monitor.waitUntilActive();
				System.out.println("received connection to mode socket");

				Socket socket = monitor.getSocket();
				OutputStream os = socket.getOutputStream();
				
				// Send data packages of different sizes
				
				while (true) {
					System.out.println("Waiting for mode change in ModeSender");
					mode = cMon.getNewMode(mode);
					
					//boolean mode = cMon.getModeUpdate();
					ModePack.pack(buffer, mode);
					//Utils.printBuffer("ClientWriteThread", size, buffer);

					// Send package
					os.write(buffer, 0, 1);
					
					// Flush data
					os.flush();
					Thread.sleep(500);
					System.out.println("----------------");
					System.out.println("wrote mode to stream: " + mode);
					System.out.println("----------------");
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
		
		System.out.println("Exiting SendMode");
	}
}
