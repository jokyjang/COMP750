package hw5;

import util.session.MessageFilter;
import util.session.MessageFilterCreator;
import util.session.SentMessage;

public class CausalitySendFilterCreator implements
		MessageFilterCreator<SentMessage> {
	
	MessageFilter<SentMessage> sentMessageFilter;
	
	public CausalitySendFilterCreator(CausalityManager cm) {
		sentMessageFilter = new SentMessageFilter(cm);
	}
	
	public MessageFilter<SentMessage> getMessageFilter() {
		// TODO Auto-generated method stub
		return sentMessageFilter;
	}

}
