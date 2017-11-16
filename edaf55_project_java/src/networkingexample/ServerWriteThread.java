package networkingexample;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import se.lth.cs.eda040.fakecamera.AxisM3006V;

public class ServerWriteThread extends Thread {

	private ServerSharedData monitor;
	private byte[] buffer;
	AxisM3006V cam = new AxisM3006V();

	public ServerWriteThread(ServerSharedData mon) {
		monitor = mon;
		buffer = new byte[AxisM3006V.IMAGE_BUFFER_SIZE];
		cam.init();
	    cam.connect();
	}

	// Receive packages of random size from active connections.
	public void run() {
		while (!monitor.isShutdown()) {
			try {
				// Blocking wait for connection
				monitor.waitUntilActive();

				Socket socket = monitor.getSocket();
				OutputStream os = socket.getOutputStream();

				// Send data packages of different sizes
				while (true) {
					int size = cam.getJPEG(buffer, 0);
					//int size = Pack.pack(buffer);
					Utils.printBuffer("ServerWriteThread", size, buffer);

					// Send package
					os.write(buffer, 0, size);

					// Flush data
					os.flush();

					// "Fake" work done before sending next package
					Thread.sleep(100);
				}
			} catch (IOException e) {
				// Something happened with the connection
				//
				// Example: the connection is closed on the client side, but
				// the server is still trying to read data.
				monitor.setActive(false);
				Utils.println("No connection on server side");
			} catch (InterruptedException e) {
				// Interrupt means shutdown
				monitor.shutdown();
				break;
			}
		}

		Utils.println("Exiting ServerWriteThread");
	}
}

// package networking;
//
// import java.awt.BorderLayout;
// import java.awt.Image;
//
// import javax.swing.ImageIcon;
// import javax.swing.JFrame;
// import javax.swing.JLabel;
// import javax.swing.SwingUtilities;
//
// import se.lth.cs.eda040.fakecamera.AxisM3006V;
//
// @SuppressWarnings("serial")
// public class SimpleViewer extends JFrame implements Runnable {
// ImageIcon icon;
// boolean firstCall = true;
//
// public static void main(String[] args) {
// SimpleViewer viewer = new SimpleViewer();
// (new Thread(viewer)).start();
// }
//
// public SimpleViewer() {
// super();
// getContentPane().setLayout(new BorderLayout());
// icon = new ImageIcon();
// JLabel label = new JLabel(icon);
// add(label, BorderLayout.CENTER);
// this.pack();
// this.setSize(640, 480);
// this.setVisible(true);
// }
//
// public void run() {
// AxisM3006V cam = new AxisM3006V();
// cam.init();
// cam.connect();
// for (int i=0; i<100; i++) {
// byte[] jpeg = new byte[AxisM3006V.IMAGE_BUFFER_SIZE];
// cam.getJPEG(jpeg, 0);
// SwingUtilities.invokeLater(new Runnable() {
// public void run() {
// refreshImage(jpeg);
// }
// });
// }
// cam.close();
// cam.destroy();
// }
//
// public void refreshImage(byte[] jpeg) {
// Image image = getToolkit().createImage(jpeg);
// getToolkit().prepareImage(image,-1,-1,null);
// icon.setImage(image);
// icon.paintIcon(this, this.getGraphics(), 0, 0);
// }
// }
