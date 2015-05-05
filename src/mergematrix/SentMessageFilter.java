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
		if(!msg.isUserMessage() || !isOTEnabled()) {
			messageProcessor.processMessage(msg);
			return;
		}
		if(msg.getUserMessage() instanceof ListEdit) {
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
			otManager.addToBuffer(otMessage);
		} else if(msg.getUserMessage() instanceof MergePolicyEdit) {
			MergePolicyEdit mergePolicyEdit = (MergePolicyEdit) msg.getUserMessage();
			OTManager otManager = otManagers.get(mergePolicyEdit.getTracingTag());
			TimeStamp timeStamp = otManager.getMergePolicyTimeStamp();
			timeStamp.incLocal();
			LocalSiteCountIncremented.newCase(msg.getSendingUser(), 
					msg.getSendingUser(), timeStamp.getLocal(),
					timeStamp.getRemote(), this);
			OTMessage otMessage = new OTMessage(
					(TimeStamp)Misc.deepCopy(timeStamp), mergePolicyEdit);
			msg.setUserMessage(otMessage);
			messageProcessor.processMessage(msg);
			otManager.addToMergePolicyBuffer(otMessage);                                     
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
