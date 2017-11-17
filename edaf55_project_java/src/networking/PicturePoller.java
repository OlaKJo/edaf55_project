package networking;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

import client.ClientMonitor;
import networkingexample.Pack;
import networkingexample.Utils;

public class PicturePoller extends Thread {

	ClientMonitor monitr;
	
	private NetMonitor netMonitor;
	private byte[] buffer;
	
	public PicturePoller(NetMonitor mon) {
		netMonitor = mon;
		buffer = new byte[2];
	}
	
	public void run() {
		while (!netMonitor.isShutdown())
		{
			try {
				// Blocking wait for connection
				netMonitor.waitUntilActive();

				Socket socket = netMonitor.getSocket();
				OutputStream os = socket.getOutputStream();
				
				Scanner reader = new Scanner(System.in);  // Reading from System.in
				
				// Send data packages of different sizes
				while (true) {
//					System.out.println("Enter a number: ");
					int n = reader.nextInt(); // Scans the next token of the input as an int.
					buffer[0] = (byte) n;
					buffer[1] = (byte) n;
					// Send package
					os.write(buffer, 0, 2);
					
					// Flush data
					os.flush();
				}
			} catch (IOException e) {
				// Something happened with the connection
				//
				// Occurs if there is an error trying to write data,
				// for instance that the connection suddenly closed.
				netMonitor.setActive(false);
				Utils.println("Failed to write to server");
			} catch (InterruptedException e) {
				// Occurs when interrupted
				netMonitor.shutdown();
				break;
			}
		}
		
		Utils.println("Exiting PicturePoller");
	}
	
}
