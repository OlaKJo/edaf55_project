package networking;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import client.ClientMonitor;


public class SendMode extends Thread {

	private NetMonitor monitor;
	private byte[] buffer;
	private ClientMonitor cMon;
	
	public SendMode(NetMonitor mon, ClientMonitor cMon) {
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
				monitor.waitUntilActive();

				Socket socket = monitor.getSocket();
				OutputStream os = socket.getOutputStream();
				boolean mode = true;
				
				// Send data packages of different sizes
				while (true) {
					//boolean mode = cMon.getModeUpdate();
					mode = !mode;
					ModePack.pack(buffer, mode);
					//Utils.printBuffer("ClientWriteThread", size, buffer);

					// Send package
					os.write(buffer, 0, 1);
					
					// Flush data
					os.flush();
					Thread.sleep(5000);
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
