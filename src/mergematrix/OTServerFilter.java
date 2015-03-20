package mergematrix;

import im.ListEdit;

import java.util.HashMap;
import java.util.Map;

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
	
//	public void filterMessage(SentMessage message) {
//		// TODO Auto-generated method stub
//		OTMessage otMessage = (OTMessage) message.getUserMessage();
//	    TimeStamp remoteTs = otMessage.getTimeStamp();
//	    ListEdit remoteOp = (ListEdit) otMessage.getMessage();
//	    OTManager otManager = otManagers.get(message.getSendingUser());
//	    
//	    TimeStamp.printAll("Server BT", remoteTs, remoteOp);
//	    
//	    remoteOp = otManager.transform(remoteOp, remoteTs);
//	    for (String user : otManagers.keySet()) {
//	      if (!user.equals(message.getSendingUser())) {
//	        SentMessage unicast = ASentMessage.toSpecificUser(message, user);
//	        OTManager otm = otManagers.get(user);
//	        TimeStamp tm = otm.getTimeStamp();
//	        tm.incLocal();
//	        OTMessage m = new OTMessage((TimeStamp) Misc.deepCopy(tm), remoteOp);
//	        TimeStamp.printAll("Server AT", tm, remoteOp);
//	        System.out.println("+---------------------------------------------+");
//	        unicast.setUserMessage(m);
//	        messageProcessor.processMessage(unicast);
//	        otm.addToBuffer(m);
//	      }
//	    }
//	}
	
	public void filterMessage(SentMessage message) {
		// TODO Auto-generated method stub
		OTMessage otMessage = (OTMessage) message.getUserMessage();
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
