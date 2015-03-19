package hw6;

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
		// TODO Auto-generated method stub
		if(msg.isUserMessage() && isOTEnabled()) {
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
			System.out.println(listEdit.getList() + ","+listEdit.getOperationName()+","
					+ listEdit.getIndex() + "," + listEdit.getElement() + ","+
					otManager.getUserName()+","+msg.getSendingUser());
			OTListEditSent.newCase(listEdit.getList(), listEdit.getOperationName(), 
					listEdit.getIndex(), listEdit.getElement(),
					timeStamp.getLocal(), timeStamp.getRemote(), 
					otManager.getUserName(), this);
			otManager.addToBuffer(otMessage);
			OTListEditBuffered.newCase(listEdit.getList(), listEdit.getOperationName(), 
					listEdit.getIndex(), listEdit.getElement(),
					timeStamp.getLocal(), timeStamp.getRemote(), 
					otManager.getUserName(), this);
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

	public void setMessageProcessor(
			MessageProcessor<SentMessage> msgProcessor) {
		// TODO Auto-generated method stub
		this.messageProcessor = msgProcessor;
	}

}
