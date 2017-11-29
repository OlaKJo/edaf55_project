package client;

public class Picture {

	public byte[] byteStream;
	public long timeStamp;
	
	public Picture(byte[] byteStream, long timeStamp){
		this.byteStream = byteStream;
		this.timeStamp = timeStamp;
	}
	
}
