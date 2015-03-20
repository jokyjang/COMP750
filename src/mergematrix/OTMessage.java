package mergematrix;

import java.io.Serializable;

public class OTMessage implements Serializable{
	private TimeStamp timeStamp;
	private Object message;
	
	public OTMessage(TimeStamp ts, Object msg) {
		timeStamp = ts;
		message = msg;
	}
	
	public TimeStamp getTimeStamp() {
		return timeStamp;
	}
	
	public Object getMessage() {
		return message;
	}
	
	public void setMessage(Object newMsg) {
		message = newMsg;
	}
}
