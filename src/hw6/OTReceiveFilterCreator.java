package hw6;

import java.util.Map;

import util.session.MessageFilter;
import util.session.MessageFilterCreator;
import util.session.ReceivedMessage;

public class OTReceiveFilterCreator implements
		MessageFilterCreator<ReceivedMessage> {
	private MessageFilter<ReceivedMessage> receiveMessageFilter;
	
	public OTReceiveFilterCreator(Map<String, OTManager> otms) {
		receiveMessageFilter = new ReceivedMessageFilter(otms);
	}

	public MessageFilter<ReceivedMessage> getMessageFilter() {
		// TODO Auto-generated method stub
		return receiveMessageFilter;
	}

}
