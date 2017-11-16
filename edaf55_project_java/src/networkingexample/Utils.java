package networkingexample;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Utils {

	public static synchronized void printBuffer(String preamble, int size, byte[] buffer) {
		StringBuffer sb = new StringBuffer();
		sb.append(preamble).append(": ");
		if (size >= 1) {
			sb.append(buffer[0]);
			for (int i=1; i<size; i++) sb.append(",").append(buffer[i]);
		}
		System.out.println(sb.toString());
		try {
			File f = new File("log.txt");
			FileWriter fw = new FileWriter(f);
			fw.append(sb.toString()+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static synchronized void println(String msg) {
		System.out.println(msg);
	}
}
