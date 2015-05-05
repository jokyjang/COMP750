package mergematrix;

import im.ListEdit;

import java.util.HashMap;
import java.util.Map;

import trace.echo.modular.OperationName;
import util.Misc;
import util.session.ASentMessage;
import util.session.MessageProcessor;
import util.session.SentMessage;
import util.session.ServerMessageFilter;

public class OTServerFilter implements ServerMessageFilter {

	private MessageProcessor<SentMessage> messageProcessor;
	// server needs to maintain all the users' buffer and time stamps
	private Map<String, OTManager> otManagers;
	
	public OTServerFilter() {
		otManagers = new HashMap<String, OTManager>();
	}
	
	public void filterMessage(SentMessage message) {
		if(!(message.getUserMessage() instanceof OTMessage)) return;
		OTMessage otMessage = (OTMessage) message.getUserMessage();
		if(otMessage.getMessage() instanceof ListEdit) {
		    TimeStamp remoteTs = otMessage.getTimeStamp();
		    ListEdit remoteOp = (ListEdit) otMessage.getMessage();
		    OTManager otManager = otManagers.get(message.getSendingUser());
		    otManager.getTimeStamp().incRemote();
		    
		    remoteOp = otManager.transform(remoteOp, remoteTs);
		    for (String user : otManagers.keySet()) {
		      if (!user.equals(message.getSendingUser())) {
		        SentMessage unicast = ASentMessage.toSpecificUser(message, user);
		        OTManager otm = otManagers.get(user);
		        TimeStamp localTs = otm.getTimeStamp();
		        localTs.incLocal();
		        OTMessage m = new OTMessage((TimeStamp) Misc.deepCopy(localTs), remoteOp);
		        unicast.setUserMessage(m);
		        messageProcessor.processMessage(unicast);
		        otm.addToBuffer(m);
		      }
		    }
		} else if(otMessage.getMessage() instanceof MergePolicyEdit) {
		    TimeStamp remoteTs = otMessage.getTimeStamp();
		    MergePolicyEdit remoteOp = (MergePolicyEdit) otMessage.getMessage();
		    OTManager otManager = otManagers.get(message.getSendingUser());
		    otManager.getMergePolicyTimeStamp().incRemote();
		    
		    remoteOp = otManager.transformMergePolicy(remoteOp, remoteTs);
		    for (String user : otManagers.keySet()) {
		      if (!user.equals(message.getSendingUser())) {
		        SentMessage unicast = ASentMessage.toSpecificUser(message, user);
		        OTManager otm = otManagers.get(user);
		        TimeStamp localTs = otm.getMergePolicyTimeStamp();
		        localTs.incLocal();
		        OTMessage m = new OTMessage((TimeStamp) Misc.deepCopy(localTs), remoteOp);
		        unicast.setUserMessage(m);
		        messageProcessor.processMessage(unicast);
		        otm.addToMergePolicyBuffer(m);
		      }
		    }
		}
	}
	
	public void setMergePolicy(OperationName a, OperationName b, MergePolicy p) {
		for(OTManager otm : otManagers.values()) {
			otm.setMergePolicy(a, b, p);
		}
	}

	public void setMessageProcessor(MessageProcessor<SentMessage> arg0) {
		// TODO Auto-generated method stub
		messageProcessor = arg0;
	}

	public void userJoined(String arg0, String arg1, String username) {
		// TODO Auto-generated method stub
		otManagers.put(username, new OTManager(username, true));
	}

	public void userLeft(String arg0, String arg1, String username) {
		// TODO Auto-generated method stub
		otManagers.remove(username);
	}

}
