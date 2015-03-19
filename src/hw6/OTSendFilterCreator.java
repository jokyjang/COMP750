package hw6;

import java.util.Map;

import util.session.MessageFilter;
import util.session.MessageFilterCreator;
import util.session.SentMessage;

public class OTSendFilterCreator implements
		MessageFilterCreator<SentMessage> {
	
	MessageFilter<SentMessage> sentMessageFilter;
	
	public OTSendFilterCreator(Map<String, OTManager> otms) {
		sentMessageFilter = new SentMessageFilter(otms);
	}
	
	public MessageFilter<SentMessage> getMessageFilter() {
		// TODO Auto-generated method stub
		return sentMessageFilter;
	}

}
