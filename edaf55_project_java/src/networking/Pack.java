
package networking;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Pack {
	public static final int HEAD_SIZE = 10;

	// ... and then the payload
	public static void unpackPayloadAndVerifyChecksum(byte[] buffer) {
		System.out.println("" + buffer[0]);

		// Also need to unpack...
	}

	public static long getTimeStamp(byte[] buffer) {
		byte[] tsData = new byte[8];
		System.arraycopy(buffer, 0, tsData, 0, 8);
		ByteBuffer tsBuffer = ByteBuffer.allocate(Long.BYTES);
		tsBuffer.put(tsData);
		tsBuffer.flip();
		tsBuffer.order(ByteOrder.LITTLE_ENDIAN);
		return tsBuffer.getLong();
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
