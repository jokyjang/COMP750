package mergematrix;

import im.ListEdit;

import java.util.Map;

import trace.ot.OTListEditReceived;
import util.session.MessageFilter;
import util.session.MessageProcessor;
import util.session.ReceivedMessage;

public class ReceivedMessageFilter 
		implements MessageFilter<ReceivedMessage> {
	private MessageProcessor<ReceivedMessage> messageProcessor;
	private Map<String, OTManager> otManagers;
	
	public ReceivedMessageFilter(Map<String, OTManager> otms) {
		this.otManagers = otms;
	}
	
	public void setMessageProcessor(
			MessageProcessor<ReceivedMessage> msgProcessor) {
		// TODO Auto-generated method stub
		this.messageProcessor = msgProcessor;
	}
	
	public void filterMessage(ReceivedMessage msg) {
		// TODO Auto-generated method stub
		//System.out.println("ReceiveMessageFilter.filterMessage: " + msg.toString());
		if(msg.isUserMessage() && isOTEnabled()) {
			OTMessage otMessage = (OTMessage) msg.getUserMessage();
			TimeStamp remoteTs = otMessage.getTimeStamp().flip();
			ListEdit remoteOp = (ListEdit) otMessage.getMessage();
			OTListEditReceived.newCase(remoteOp.getList(), 
					remoteOp.getOperationName(), remoteOp.getIndex(), 
					remoteOp.getElement(), remoteTs.getLocal(),
					remoteTs.getRemote(), msg.getClientName(), this);
			OTManager otManager = otManagers.get(remoteOp.getList());
			remoteOp = otManager.transform(remoteOp, remoteTs);
			otManager.getTimeStamp().incRemote();
			msg.setUserMessage(remoteOp);
			messageProcessor.processMessage(msg);
		} else {
			messageProcessor.processMessage(msg);
		}
	}
	
	public boolean isOTEnabled() {
		for(OTManager otManager : otManagers.values()) {
			if(otManager.isEnabled()) return true;
		}
		return false;
	}
}
