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
		if(msg.isUserMessage() && isOTEnabled() &&
				msg.getUserMessage() instanceof OTMessage) {
			OTMessage otMessage = (OTMessage) msg.getUserMessage();
			TimeStamp remoteTs = otMessage.getTimeStamp();
			ListEdit remoteOp = (ListEdit) otMessage.getMessage();
			
			TimeStamp.printAll("Received Message", remoteTs, remoteOp);
			/*
			OTListEditReceived.newCase(remoteOp.getList(), 
					remoteOp.getOperationName(), remoteOp.getIndex(), 
					remoteOp.getElement(), remoteTs.getLocal(),
					remoteTs.getRemote(), msg.getClientName(), this);
					*/
			OTManager otManager = otManagers.get(remoteOp.getList());
			remoteOp = otManager.transform(remoteOp, remoteTs);
			otManager.getTimeStamp().incRemote();
			msg.setUserMessage(remoteOp);
			TimeStamp.printAll("Trnsd Rcved Mse", otManager.getTimeStamp(), remoteOp);
			messageProcessor.processMessage(msg);
		} else {
			if(msg.getUserMessage() instanceof MergePolicyEdit) {
				System.out.println("I've received a MergePolicyEdit!");
			}
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
