package mergematrix;

import im.ListEdit;

import java.util.ArrayList;
import java.util.List;

import trace.echo.modular.OperationName;
import trace.ot.InitialOTTimeStampCreated;
import trace.ot.LocalEditCountIncremented;
import trace.ot.TransformationResult;
import util.Misc;


public class OTManager {
	private boolean enabled;
	private boolean isServer;
	private String userName;
	private TimeStamp timeStamp;
	private List<OTMessage> buffer;
	
	public OTManager(String un, boolean is) {
		enabled = false;
		userName = un;
		isServer = is;
		timeStamp = new TimeStamp();
		InitialOTTimeStampCreated.newCase(un, un, 
				timeStamp.getLocal(), timeStamp.getRemote(), 
				is, this);
		buffer = new ArrayList<OTMessage>();
	}
	
	public TimeStamp getTimeStamp() {
		return timeStamp;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void enable() {
		enabled = true;
	}
	
	public void disable() {
		enabled = false;
	}

	public String getUserName() {
		return userName;
	}

	public void addToBuffer(OTMessage msg) {
		this.buffer.add(msg);
	}
	
	public boolean isServer() {
		return isServer;
	}
	
	public ListEdit transform(ListEdit remoteOp, TimeStamp remoteTs) {
		ListEdit newOp = (ListEdit) Misc.deepCopy(remoteOp);
		List<OTMessage> toBeRemoved = new ArrayList<OTMessage>();
		for(OTMessage otMessage : buffer) {
			TimeStamp localTs = otMessage.getTimeStamp();
			if(!localTs.isConcurrent(remoteTs)) {
				toBeRemoved.add(otMessage);
			}
		}
		buffer.removeAll(toBeRemoved);
		for(OTMessage otMessage : buffer) {
			TimeStamp localTs = otMessage.getTimeStamp();
			ListEdit localOp = (ListEdit) otMessage.getMessage();
			if(!localTs.isConcurrent(remoteTs)) continue;
			newOp = transformOP(remoteOp, localOp, !isServer());
			TransformationResult.newCase(localOp.getList(), 
					localOp.getOperationName(), localOp.getIndex(),
					localOp.getElement(), localTs.getLocal(),
					localTs.getRemote(), userName, isServer, this);
			localOp = transformOP(localOp, remoteOp, isServer());
			LocalEditCountIncremented.newCase(localOp.getList(), 
					localOp.getOperationName(), localOp.getIndex(),
					localOp.getElement(), localTs.getLocal(),
					localTs.getRemote(), userName, this);
			localTs.incRemote();
			LocalEditCountIncremented.newCase(localOp.getList(), 
					localOp.getOperationName(), localOp.getIndex(),
					localOp.getElement(), localTs.getLocal(),
					localTs.getRemote(), userName, this);
			otMessage.setMessage(localOp);
			remoteOp = newOp;
		}
		return newOp;
	}
	
	/**
	 * Transform remoteOp with respect to localOp
	 * @param remoteOp	- remote operation
	 * @param localOp	- local operation
	 * @param remote	- remoteOp has higher priority
	 * @return
	 */
	private ListEdit transformOP(ListEdit remoteOp, ListEdit localOp, boolean remote) {
		if(remoteOp.getOperationName().equals(OperationName.ADD)
				&& localOp.getOperationName().equals(OperationName.ADD)) {
			return transformII(remoteOp, localOp, remote);
		} else if(remoteOp.getOperationName().equals(OperationName.DELETE)
				&& localOp.getOperationName().equals(OperationName.DELETE)) {
			return transformDD(remoteOp, localOp, remote);
		} else if(remoteOp.getOperationName().equals(OperationName.ADD)
				&& localOp.getOperationName().equals(OperationName.DELETE)) {
			return transformID(remoteOp, localOp, remote);
		} else if(remoteOp.getOperationName().equals(OperationName.DELETE)
				&& localOp.getOperationName().equals(OperationName.ADD)) {
			return transformDI(remoteOp, localOp, remote);
		} else {
			System.out.println(remoteOp.getOperationName() + ", " + localOp.getOperationName());
			return (ListEdit) Misc.deepCopy(remoteOp);
		}
	}
	
	private ListEdit transformDI(ListEdit remoteOp, ListEdit localOp, boolean remote) {
		ListEdit newOp = (ListEdit) Misc.deepCopy(remoteOp);
		if(remoteOp.getIndex() > localOp.getIndex()) {
			newOp.setIndex(remoteOp.getIndex() + 1);
		} else if(remoteOp.getIndex() == localOp.getIndex()) {
			newOp.setIndex(remoteOp.getIndex() + 1);
		}
		return newOp;
	}

	private ListEdit transformID(ListEdit remoteOp, ListEdit localOp, boolean remote) {
		ListEdit newOp = (ListEdit) Misc.deepCopy(remoteOp);
		if(remoteOp.getIndex() > localOp.getIndex()) {
			newOp.setIndex(remoteOp.getIndex() - 1);
		} else if(remoteOp.getIndex() == localOp.getIndex()) {
			
		}
		return newOp;
	}

	// transform insertion operation with respect to insertion
	private ListEdit transformII(ListEdit remoteOp, ListEdit localOp, boolean remote) {
		ListEdit newOp = (ListEdit) Misc.deepCopy(remoteOp);
		if((remoteOp.getIndex() > localOp.getIndex())) {
			newOp.setIndex(remoteOp.getIndex() + 1);
		} else if (remoteOp.getIndex() == localOp.getIndex()) {
			newOp.setOperationName(OperationName.DELETE);
			newOp.setIndex(remoteOp.getIndex());
			newOp.setElement(localOp.getElement());
		}
		return newOp;
	}
	
	private ListEdit transformDD(ListEdit remoteOp, ListEdit localOp, boolean remote) {
		ListEdit newOp = (ListEdit) Misc.deepCopy(remoteOp);
		if((remoteOp.getIndex() > localOp.getIndex())
				|| (remoteOp.getIndex() == localOp.getIndex() && isServer())) {
			newOp.setIndex(remoteOp.getIndex() - 1);
		} else if(remoteOp.getIndex() == localOp.getIndex() && !isServer()) {
			newOp.setOperationName(OperationName.REPLACE);
		}
		return newOp;
	}
}
