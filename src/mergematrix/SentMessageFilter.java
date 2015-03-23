package mergematrix;

import im.ListEdit;

import java.util.Map;

import trace.ot.LocalSiteCountIncremented;
import trace.ot.OTListEditBuffered;
import trace.ot.OTListEditSent;
import util.Misc;
import util.session.MessageFilter;
import util.session.MessageProcessor;
import util.session.SentMessage;

public class SentMessageFilter implements MessageFilter<SentMessage> {
	MessageProcessor<SentMessage> messageProcessor;
	Map<String, OTManager> otManagers;
	
	public SentMessageFilter(Map<String, OTManager> otms) {
		otManagers = otms;
	}

	public void filterMessage(SentMessage msg) {
		if(msg.isUserMessage() && isOTEnabled() &&
				msg.getUserMessage() instanceof ListEdit) {
			ListEdit listEdit = (ListEdit) msg.getUserMessage();
			OTManager otManager = otManagers.get(listEdit.getList());
			TimeStamp timeStamp = otManager.getTimeStamp();
			timeStamp.incLocal();
			LocalSiteCountIncremented.newCase(msg.getSendingUser(), 
					msg.getSendingUser(), timeStamp.getLocal(),
					timeStamp.getRemote(), this);
			OTMessage otMessage = new OTMessage(
					(TimeStamp)Misc.deepCopy(timeStamp), listEdit);
			msg.setUserMessage(otMessage);
			messageProcessor.processMessage(msg);
			
			TimeStamp.printAll("Sent Message", timeStamp, listEdit);
			
			/*OTListEditSent.newCase(listEdit.getList(), listEdit.getOperationName(), 
					listEdit.getIndex(), listEdit.getElement(),
					timeStamp.getLocal(), timeStamp.getRemote(), 
					otManager.getUserName(), this);
					*/
			otManager.addToBuffer(otMessage);
			/*OTListEditBuffered.newCase(listEdit.getList(), listEdit.getOperationName(), 
					listEdit.getIndex(), listEdit.getElement(),
					timeStamp.getLocal(), timeStamp.getRemote(), 
					otManager.getUserName(), this);
					*/
		} else {
			if(msg.getUserMessage() instanceof MergePolicyEdit) {
				System.out.println("I've sent an MergePolicyEdit message!");
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

	public void setMessageProcessor(
			MessageProcessor<SentMessage> msgProcessor) {
		// TODO Auto-generated method stub
		this.messageProcessor = msgProcessor;
	}

}
