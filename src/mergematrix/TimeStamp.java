package mergematrix;

import im.ListEdit;

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
		return (local > theOther.getRemote()) && 
				(remote < theOther.getLocal());
	}
	
	public String toString() {
		return "["+Integer.toString(local)+","+Integer.toString(remote)+"]";
	}
	
	public static void printAll(String prefix, TimeStamp ts, ListEdit le) {
		System.out.print("<"+prefix+">: " + ts.toString() + " ");
		System.out.println(le.getList() + ","+le.getOperationName()+","
				+ le.getIndex() + "," + le.getElement());
	}

	public static void printAllMergePolicyEdit(String prefix,
			TimeStamp timeStamp, MergePolicyEdit mergePolicyEdit) {
		System.out.print("<"+prefix+">: " + timeStamp.toString() + " ");
		System.out.println(mergePolicyEdit.getServer()+","+mergePolicyEdit.getClient()
				+","+mergePolicyEdit.getPolicy());
	}
}
