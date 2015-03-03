package hw5;

import java.io.Serializable;
import java.util.Map;

public class TimeStampVectorWithMessage implements Serializable{
	Map<String, Integer> timeStamp;
	Object message;
	
	public TimeStampVectorWithMessage(Map<String, Integer> ts, Object msg) {
		timeStamp = ts;
		message = msg;
	}
	
	public Map<String, Integer> getTimeStamp() {
		return timeStamp;
	}
	
	public Object getMessage() {
		return message;
	}
}
