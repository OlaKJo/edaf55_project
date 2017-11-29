
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
<<<<<<< HEAD
//		byte[] tsData = new byte[8];
//		System.arraycopy(buffer, 0, tsData, 0, 8);
//		ByteBuffer tsBuffer = ByteBuffer.allocate(Long.BYTES);
//		tsBuffer.put(tsData);
//		tsBuffer.flip();
//		tsBuffer.order(ByteOrder.BIG_ENDIAN);
//		byte[] rearranged = tsBuffer.array();
//		//long temp = tsBuffer.getLong();
//		
//		BigInteger bigInt = new BigInteger(1,rearranged);
//		long print = Long.parseUnsignedLong(bigInt.toString());
//		System.out.println("timestamp is now: " + print);
//		for(int i = 0; i < tsData.length; i++) {
//			System.out.println(tsData[i] + " ");
//		}
//		return print;
		
		byte[] tsData = new byte[4];
		System.arraycopy(buffer, 0, tsData, 0, 4);
		ByteBuffer sizeBuffer = ByteBuffer.allocate(Integer.BYTES);
		sizeBuffer.put(tsData);
		sizeBuffer.flip();
		sizeBuffer.order(ByteOrder.LITTLE_ENDIAN);
	    int returnInt = sizeBuffer.getInt();
	    System.out.println("The time_stamp is now: " + returnInt);
	    return returnInt;
=======
		byte[] tsData = new byte[8];
		System.arraycopy(buffer, 0, tsData, 0, 8);
//		ByteBuffer tsBuffer = ByteBuffer.allocate(Long.BYTES);
//		tsBuffer.put(tsData);
//		tsBuffer.flip();
//		tsBuffer.order(ByteOrder.LITTLE_ENDIAN);
		BigInteger bigTime = new BigInteger(1, tsData);
		
		return 1l;
>>>>>>> bd697ba9e26b450912add3807b570d8ed230d500
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
