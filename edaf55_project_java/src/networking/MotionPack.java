package networking;

import java.util.StringTokenizer;

public class MotionPack {
	
	public static final int HEAD_SIZE = 58;
	
	public static void pack(byte[] buffer) {

		String sendString = "GET / ";
		byte[] b = sendString.getBytes();
//		System.out.println("message size: " + b.length);

		for(int i = 0; i < b.length; i++)
		{
//			System.out.println(b[i]);
			byte test = b[i];
			buffer[i] = test;
		}
	}

	public static int[] getTimes(byte[] buffer) {
		int[] timeBuffer = new int[3];
			
		char[] timeData = new char[buffer.length - MotionPack.HEAD_SIZE];
		for (int i = 64; i < buffer.length; i++) {
			timeData[i - 64] = (char) buffer[i];
		}
		
		String times = new String(timeData);
		StringTokenizer st = new StringTokenizer(times.replaceAll("\\s+",""), ":");
		int i = 0;
		while(st.hasMoreTokens()) {
			String str = st.nextToken().trim();
			//System.out.println(str);
			//System.out.println("string size: " + str.length());
			
			timeBuffer[i++] = Integer.parseInt(str);
		}
		
		return timeBuffer;
	}

	public static int getTimeSize(byte[] buffer) {
		char[] b = new char[3];
		for (int i = 0; i < b.length; i++) {
//			System.out.println((char) buffer[i+31]);
			b[i] = (char) buffer[i+31];
		}
		return Integer.parseInt(new String(b).trim());
	}
}

