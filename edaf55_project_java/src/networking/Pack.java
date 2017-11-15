
package networking;

public class Pack {
	public static final int HEAD_SIZE = 1;
	
	// ... and then the payload
	public static void unpackPayloadAndVerifyChecksum(byte[] buffer) {
		System.out.println("" + buffer[0]);
		
		// Also need to unpack...
	}
}
