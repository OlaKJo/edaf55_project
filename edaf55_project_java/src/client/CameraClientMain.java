package client;

import networking.*;

public class CameraClientMain {

	//Manage client side classes
	public static void main(String[] args) {
		ClientMonitor cm = new ClientMonitor();
		ModeNetStarter mn = new ModeNetStarter(cm, "127.0.0.1", 8000);
		mn.startMe();
	}
	
}
