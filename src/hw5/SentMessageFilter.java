package hw5;

import java.util.Map;

import trace.causal.LocalCountIncrementedInSiteVectorTimeStamp;
import trace.causal.VectorTimeStampedMessageSent;
import util.Misc;
import util.session.MessageFilter;
import util.session.MessageProcessor;
import util.session.SentMessage;

public class SentMessageFilter implements MessageFilter<SentMessage> {
	MessageProcessor<SentMessage> messageProcessor;
	CausalityManager causalityManager;
	
	public SentMessageFilter(CausalityManager cm) {
		causalityManager = cm;
	}

	public void filterMessage(SentMessage msg) {
		// TODO Auto-generated method stub
		//System.out.println("SendMessageFilter.filterMessage(" + msg.toString()+")");
		if(msg.isUserMessage()) {
			Object message =  msg.getUserMessage();
			
			Map<String, Integer> ts = causalityManager.getTimeStamps();
			ts.put(msg.getSendingUser(), 1 + ts.get(msg.getSendingUser()));
			Map<String, Integer> nts = (Map<String, Integer>) Misc.deepCopy(ts);
			LocalCountIncrementedInSiteVectorTimeStamp.newCase(nts, this);
			TimeStampVectorWithMessage tsvwm = new TimeStampVectorWithMessage(nts, message);
			msg.setUserMessage(tsvwm);
			VectorTimeStampedMessageSent.newCase(msg, nts, this);
		}
		messageProcessor.processMessage(msg);
	}

	public void setMessageProcessor(MessageProcessor<SentMessage> msgProcessor) {
		// TODO Auto-generated method stub
		this.messageProcessor = msgProcessor;
	}

}
