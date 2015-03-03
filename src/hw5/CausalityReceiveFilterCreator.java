package hw5;

import util.session.MessageFilter;
import util.session.MessageFilterCreator;
import util.session.ReceivedMessage;

public class CausalityReceiveFilterCreator implements
		MessageFilterCreator<ReceivedMessage> {
	MessageFilter<ReceivedMessage> receiveMessageFilter;
	
	public CausalityReceiveFilterCreator(CausalityManager cm) {
		receiveMessageFilter = new ReceivedMessageFilter(cm);
	}

	public MessageFilter<ReceivedMessage> getMessageFilter() {
		// TODO Auto-generated method stub
		return receiveMessageFilter;
	}

}
