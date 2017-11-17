package networking;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import client.ClientMonitor;

public class PictureReciever extends Thread {

	ClientMonitor monitr;

	private NetMonitor netMonitor;
	private byte[] buffer;
	private final int MAXIMUM_PICTURE_SIZE = 50000;

	public PictureReciever(NetMonitor mon) {
		netMonitor = mon;
		buffer = new byte[MAXIMUM_PICTURE_SIZE];
	}

	// Receive packages of random size from active connections.
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
					int size = Pack.HEAD_SIZE;
					int n = 0;
					while ((n = is.read(buffer, n, size)) > 0) {
						size -= n;
					}
					if (size != 0)
						break;

					long ts = Pack.getTimeStamp(buffer);
					int pictureSize = Pack.getPictureSize(buffer);
					System.out.println("pictureSize: " + pictureSize);
					System.out.print("Timestamp: " + ts);
					
					// Read image data
					size = pictureSize;
					n = 0;
					while ((n = is.read(buffer, n + Pack.HEAD_SIZE, size)) > 0) {
						System.out.println(size);
						size -= n;
					}
					if (size != 0)
						break;

					byte[] imageData = new byte[pictureSize];
					System.arraycopy(buffer, Pack.HEAD_SIZE, imageData, 0, pictureSize);

					// ImageIcon a = new ImageIcon(imageData);
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
		System.out.println("Exiting PictureReciever");
	}
}
