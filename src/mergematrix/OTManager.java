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
	private MergeMatrix mergeMatrix;
	
	private List<OTMessage> mergePolicyBuffer;
	private TimeStamp mergePolicyTimeStamp;
	
	public OTManager(String un, boolean is) {
		enabled = true;
		userName = un;
		isServer = is;
		timeStamp = new TimeStamp();
		mergePolicyTimeStamp = new TimeStamp();
		InitialOTTimeStampCreated.newCase(un, un, 
				timeStamp.getLocal(), timeStamp.getRemote(), 
				is, this);
		buffer = new ArrayList<OTMessage>();
		mergePolicyBuffer = new ArrayList<OTMessage>();
		mergeMatrix = new SimpleListMergeMatrix();
	}
	
	public TimeStamp getTimeStamp() {
		return timeStamp;
	}
	
	public TimeStamp getMergePolicyTimeStamp() {
		return mergePolicyTimeStamp;
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
	
	public void addToMergePolicyBuffer(OTMessage msg) {
		this.mergePolicyBuffer.add(msg);
	}
	
	public boolean isServer() {
		return isServer;
	}
	
	public MergeMatrix getMergeMatrix() {
		return this.mergeMatrix;
	}
	
	public void setMergePolicy(OperationName a, OperationName b, MergePolicy p) {
		mergeMatrix.set(a, b, p);
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
		if(remoteOp.getOperationName() == OperationName.NULL 
				|| localOp.getOperationName() == OperationName.NULL) return remoteOp;
		if(remoteOp.getOperationName().equals(OperationName.ADD)
				&& localOp.getOperationName().equals(OperationName.ADD)) {
			return transformII(remoteOp, localOp, remote);
		} else if(remoteOp.getOperationName().equals(OperationName.DELETE)
				&& localOp.getOperationName().equals(OperationName.DELETE)) {
			return transformDD(remoteOp, localOp, remote);
		} else if(remoteOp.getOperationName().equals(OperationName.REPLACE)
				&& localOp.getOperationName().equals(OperationName.REPLACE)) {
			return transformRR(remoteOp, localOp, remote);
		} else if(remoteOp.getOperationName().equals(OperationName.ADD)
				&& localOp.getOperationName().equals(OperationName.DELETE)) {
			return transformID(remoteOp, localOp, remote);
		} else if(remoteOp.getOperationName().equals(OperationName.DELETE)
				&& localOp.getOperationName().equals(OperationName.ADD)) {
			return transformDI(remoteOp, localOp, remote);
		} else if(remoteOp.getOperationName().equals(OperationName.REPLACE)
				&& localOp.getOperationName().equals(OperationName.ADD)) {
			return transformRI(remoteOp, localOp, remote);
		} else if (remoteOp.getOperationName().equals(OperationName.ADD)
				&& localOp.getOperationName().equals(OperationName.REPLACE)) {
			return transformIR(remoteOp, localOp, remote);
		} else if (remoteOp.getOperationName().equals(OperationName.DELETE)
				&& localOp.getOperationName().equals(OperationName.REPLACE)) {
			return transformDR(remoteOp, localOp, remote);
		} else if(remoteOp.getOperationName().equals(OperationName.REPLACE)
				&& localOp.getOperationName().equals(OperationName.DELETE)) {
			return transformRD(remoteOp, localOp, remote);
		} else {
			System.out.println(remoteOp.getOperationName() + ", " + localOp.getOperationName());
			return (ListEdit) Misc.deepCopy(remoteOp);
		}
	}
	
	private ListEdit transformRD(ListEdit remoteOp, ListEdit localOp,
			boolean remote) {
		// TODO Auto-generated method stub
		ListEdit newOp = (ListEdit) Misc.deepCopy(remoteOp);
		if(newOp.getIndex() > localOp.getIndex()) {
			newOp.setIndex(newOp.getIndex() - 1);
		} else if(newOp.getIndex() == localOp.getIndex()) {
			switch(mergeMatrix.get(OperationName.REPLACE, OperationName.DELETE)) {
			case NONE:
				newOp.setOperationName(OperationName.ADD);
				newOp.setElement(localOp.getElement());
				break;
			case SERVER:
				if(remote) newOp.setOperationName(OperationName.ADD);
				else newOp.setOperationName(OperationName.NULL);
				break;
			case BOTH:
			case CLIENT:
			default:
				if(remote) newOp.setOperationName(OperationName.NULL);
				else newOp.setOperationName(OperationName.ADD);
				break;
			}
		}
		return newOp;
	}

	private ListEdit transformDR(ListEdit remoteOp, ListEdit localOp,
			boolean remote) {
		// TODO Auto-generated method stub
		ListEdit newOp = (ListEdit) Misc.deepCopy(remoteOp);
		if(newOp.getIndex() == localOp.getIndex()) {
			switch(mergeMatrix.get(OperationName.DELETE, OperationName.REPLACE)) {
			case NONE:
				newOp.setOperationName(OperationName.REPLACE);
				break;
			case CLIENT:
				if(remote) newOp.setOperationName(OperationName.NULL);
				else newOp.setElement(localOp.getElement());
				break;
			case BOTH:
			case SERVER:
			default:
				if(remote) newOp.setElement(localOp.getElement());
				else newOp.setOperationName(OperationName.NULL);
				break;
			}
		}
		return newOp;
	}

	private ListEdit transformRR(ListEdit remoteOp, ListEdit localOp,
			boolean remote) {
		// TODO Auto-generated method stub
		ListEdit newOp = (ListEdit) Misc.deepCopy(remoteOp);
		if(newOp.getIndex() == localOp.getIndex()) {
			switch(mergeMatrix.get(OperationName.REPLACE, OperationName.REPLACE)) {
			case CLIENT:
				if(remote) newOp.setOperationName(OperationName.NULL);
				else newOp.setElement(localOp.getElement());
				break;
			case NONE:
			case BOTH:
			case SERVER:
			default:
				if(remote) newOp.setElement(localOp.getElement());
				else newOp.setOperationName(OperationName.NULL);
				break;
			}
		}
		return newOp;
	}

	private ListEdit transformIR(ListEdit remoteOp, ListEdit localOp,
			boolean remote) {
		// TODO Auto-generated method stub
		ListEdit newOp = (ListEdit) Misc.deepCopy(remoteOp);
		if(newOp.getIndex() == localOp.getIndex()) {
			// TODO Handle when different merge policy
			switch(mergeMatrix.get(OperationName.ADD, OperationName.REPLACE)) {
			case NONE:
				//TODO cannot accept none cause we don't know the original character
				//break;
			case BOTH:
			case SERVER:
			case CLIENT:
			default:
				// INSERTION: do nothing, no change
				break;
			}
		}
		return newOp;
	}

	private ListEdit transformRI(ListEdit remoteOp, ListEdit localOp,
			boolean remote) {
		// TODO Auto-generated method stub
		ListEdit newOp = (ListEdit) Misc.deepCopy(remoteOp);
		if(localOp.getIndex() < newOp.getIndex()) {
			newOp.setIndex(newOp.getIndex() + 1);
		} else if(localOp.getIndex() == newOp.getIndex()) {
			// TODO handle when different merge policy
			switch(mergeMatrix.get(OperationName.REPLACE, OperationName.ADD)) {
			case NONE:
			case SERVER:
			case CLIENT:
			case BOTH:
			default:
				newOp.setIndex(newOp.getIndex() + 1);
				break;
			}
		}
		return newOp;
	}

	private ListEdit transformDI(ListEdit remoteOp, ListEdit localOp, boolean remote) {
		ListEdit newOp = (ListEdit) Misc.deepCopy(remoteOp);
		if(newOp.getIndex() > localOp.getIndex()) {
			newOp.setIndex(newOp.getIndex() + 1);
		} else if(newOp.getIndex() == localOp.getIndex()) {
			switch(mergeMatrix.get(OperationName.DELETE, OperationName.ADD)) {
			case NONE:
				newOp.setElement(localOp.getElement());
				break;
			case SERVER:
			case CLIENT:
			case BOTH:
			default:
				newOp.setIndex(newOp.getIndex() + 1);
			}
		}
		return newOp;
	}

	private ListEdit transformID(ListEdit remoteOp, ListEdit localOp, boolean remote) {
		ListEdit newOp = (ListEdit) Misc.deepCopy(remoteOp);
		if(newOp.getIndex() > localOp.getIndex()) {
			newOp.setIndex(newOp.getIndex() - 1);
		} else if(newOp.getIndex() == localOp.getIndex()) {
			switch(mergeMatrix.get(OperationName.ADD, OperationName.DELETE)) {
			case NONE:
				newOp.setElement(localOp.getElement());
				break;
			case SERVER:
			case CLIENT:
			case BOTH:
			default:
				// DO NOTHING
				break;
			}
		}
		return newOp;
	}

	// transform insertion operation with respect to insertion
	private ListEdit transformII(ListEdit remoteOp, ListEdit localOp, boolean remote) {
		ListEdit newOp = (ListEdit) Misc.deepCopy(remoteOp);
		//System.out.println("transformII" + mergeMatrix.get(OperationName.ADD, OperationName.ADD));
		if((remoteOp.getIndex() > localOp.getIndex())) {
			newOp.setIndex(remoteOp.getIndex() + 1);
		} else if (remoteOp.getIndex() == localOp.getIndex()) {
			switch(mergeMatrix.get(OperationName.ADD, OperationName.ADD)) {
			case NONE:
				newOp.setOperationName(OperationName.DELETE);
				newOp.setIndex(remoteOp.getIndex());
				newOp.setElement(localOp.getElement());
				break;
			case SERVER:
				if(remote) newOp.setOperationName(OperationName.REPLACE);
				else newOp.setOperationName(OperationName.NULL);
				break;
			case CLIENT:
				if(remote) newOp.setOperationName(OperationName.NULL);
				else newOp.setOperationName(OperationName.REPLACE);
				break;
			case BOTH:
			default:
				if(!remote) {
					newOp.setIndex(newOp.getIndex() + 1);
				}
				break;
			}
		}
		return newOp;
	}
	
	private ListEdit transformDD(ListEdit remoteOp, ListEdit localOp, boolean remote) {
		ListEdit newOp = (ListEdit) Misc.deepCopy(remoteOp);
		if((newOp.getIndex() > localOp.getIndex())) {
			newOp.setIndex(remoteOp.getIndex() - 1);
		} else if(remoteOp.getIndex() == localOp.getIndex()) {
			switch(mergeMatrix.get(OperationName.DELETE, OperationName.DELETE)) {
			case NONE:
				newOp.setOperationName(OperationName.ADD);
				break;
			case BOTH:
			case SERVER:
			case CLIENT:
			default:
				newOp.setOperationName(OperationName.NULL);
				break;
			}
		}
		return newOp;
	}

	public void setMergeMatrix(MergeMatrix aMergeMatrix) {
		// TODO Auto-generated method stub
		mergeMatrix = aMergeMatrix;
		if(mergeMatrix == null) {
			mergeMatrix = new SimpleListMergeMatrix();
		}
	}
	
	/******************************************************************************************
	 * Below this line are code concerning MergePolicyEdit
	 ******************************************************************************************/
	/* 
	 * @param remoteOp
	 * @param remoteTs
	 * @return
	 */
	public MergePolicyEdit transformMergePolicy(MergePolicyEdit remoteOp, TimeStamp remoteTs) {
		MergePolicyEdit newOp = (MergePolicyEdit) Misc.deepCopy(remoteOp);
		List<OTMessage> toBeRemoved = new ArrayList<OTMessage>();
		for(OTMessage otMessage : mergePolicyBuffer) {
			TimeStamp localTs = otMessage.getTimeStamp();
			if(!localTs.isConcurrent(remoteTs)) {
				toBeRemoved.add(otMessage);
			}
		}
		mergePolicyBuffer.removeAll(toBeRemoved);
		for(OTMessage otMessage : mergePolicyBuffer) {
			TimeStamp localTs = otMessage.getTimeStamp();
			MergePolicyEdit localOp = (MergePolicyEdit) otMessage.getMessage();
			if(!localTs.isConcurrent(remoteTs)) continue;
			newOp = transformMergePolicyOP(remoteOp, localOp, !isServer());
//			TransformationResult.newCase(localOp.getList(),
//					localOp.getOperationName(), localOp.getIndex(),
//					localOp.getElement(), localTs.getLocal(),
//					localTs.getRemote(), userName, isServer, this);
			localOp = transformMergePolicyOP(localOp, remoteOp, isServer());
//			LocalEditCountIncremented.newCase(localOp.getList(),
//					localOp.getOperationName(), localOp.getIndex(),
//					localOp.getElement(), localTs.getLocal(),
//					localTs.getRemote(), userName, this);
			localTs.incRemote();
//			LocalEditCountIncremented.newCase(localOp.getList(),
//					localOp.getOperationName(), localOp.getIndex(),
//					localOp.getElement(), localTs.getLocal(),
//					localTs.getRemote(), userName, this);
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
	private MergePolicyEdit transformMergePolicyOP(MergePolicyEdit remoteOp, MergePolicyEdit localOp, boolean remote) {
		MergePolicyEdit newOp = (MergePolicyEdit) Misc.deepCopy(remoteOp);
		if(newOp.getServer().equals(localOp.getServer())
				&& newOp.getClient().equals(localOp.getClient())) {
			if(remote) newOp.setPolicy(localOp.getPolicy());
		}
		return newOp;
	}
}
