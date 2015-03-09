package mergematrix;

import java.io.Serializable;

public class TimeStamp implements Serializable {
	private int local;
	private int remote;
	
	public TimeStamp() {
		this(0, 0);
	}
	
	private TimeStamp(int l, int r) {
		local = l;
		remote = r;
	}
	
	public int getLocal() {
		return local;
	}
	public int getRemote() {
		return remote;
	}
	public void incLocal() {
		++local;
	}
	public void incRemote() {
		++remote;
	}
	public TimeStamp flip() {
		return new TimeStamp(remote, local);
	}
	public boolean isConcurrent(TimeStamp theOther) {
		return (local != theOther.getRemote()) && 
				(remote != theOther.getLocal());
	}
}
