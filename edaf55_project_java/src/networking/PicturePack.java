
package networking;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PicturePack {
	public static final int HEAD_SIZE = 10;

	// ... and then the payload
	public static void unpackPayloadAndVerifyChecksum(byte[] buffer) {
		System.out.println("" + buffer[0]);

		// Also need to unpack...
	}

	public static long getTimeStamp(byte[] buffer) {
		
		byte[] tsData = new byte[4];
		System.arraycopy(buffer, 0, tsData, 0, 4);
		ByteBuffer sizeBuffer = ByteBuffer.allocate(Integer.BYTES);
		sizeBuffer.put(tsData);
		sizeBuffer.flip();
		sizeBuffer.order(ByteOrder.LITTLE_ENDIAN);
	    int returnInt = sizeBuffer.getInt();
	    System.out.println("The time_stamp is now: " + returnInt);
	    return returnInt;

	}

	public static int getPictureSize(byte[] buffer) {
		byte[] sizeData = new byte[4];
		System.arraycopy(buffer, 8, sizeData, 0, 2);
		ByteBuffer sizeBuffer = ByteBuffer.allocate(Integer.BYTES);
		sizeBuffer.put(sizeData);
		sizeBuffer.flip();
		sizeBuffer.order(ByteOrder.LITTLE_ENDIAN);
	    return sizeBuffer.getInt();
	}

}
