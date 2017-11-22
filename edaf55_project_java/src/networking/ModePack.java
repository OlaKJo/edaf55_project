package networking;

public class ModePack {
	
	// Pack data into byte array buffer
	// Return number of bytes to send
	public static void pack(byte[] buffer, boolean movieMode) {

		if(movieMode) buffer[0] = 0x01;
		else buffer[0] = 0x00;
	}
}
