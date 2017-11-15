package Client;

import java.io.IOException;
import java.io.InputStream;

import networking.*;

public class PictureReciever extends Thread {

	ClientMonitor monitr;
	
	private NetMonitor netMonitor;
	private byte[] buffer;
	
	public PictureReciever(NetMonitor mon) {
		netMonitor = mon;
		buffer = new byte[8192];
	}
	
	// Receive packages of random size from active connections.
	public void run() {
		while (!netMonitor.isShutdown())
		{
			try {
				// Wait for active connection
				netMonitor.waitUntilActive();
				
				InputStream is = netMonitor.getSocket().getInputStream();
				
				// Receive data packages of different sizes
				while (true) {
					// Read header
					int size = Pack.HEAD_SIZE;
					int n = 0;
					while ((n = is.read(buffer, n, size)) > 0) {
						size -= n;
					}
					if (size != 0) break;
					
					Pack.unpackPayloadAndVerifyChecksum(buffer);
				}
			} catch (IOException e) {
				// Something happened with the connection
				//
				// Example: the connection is closed on the server side, but
				// the client is still trying to write data.
				netMonitor.setActive(false);
				System.out.println("No connection on client side");
			} catch (InterruptedException e) {
				// Occurs when interrupted
				netMonitor.shutdown();
				break;
			}
		}
		
		// No resources to dispose since this is the responsibility
		// of the shutdown thread.
		System.out.println("Exiting ClientReadThread");
	}
	
}
