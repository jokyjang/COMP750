package mergematrix;

import im.ListEdit;

import java.util.HashMap;
import java.util.Map;

import util.session.MessageProcessor;
import util.session.SentMessage;
import util.session.ServerMessageFilter;

public class OTServerFilters implements ServerMessageFilter {
	private MessageProcessor<SentMessage> messageProcessor;
	private Map<String, OTServerFilter> filters;
	private OTServerFilter historyFilter;
	private OTServerFilter topicFilter;
	
	public OTServerFilters() {
		filters = new HashMap<String, OTServerFilter>();
		historyFilter = new OTServerFilter();
		topicFilter = new OTServerFilter();
		filters.put(ApplicationTags.IM, historyFilter);
		filters.put(ApplicationTags.EDITOR, topicFilter);
	}

	public void filterMessage(SentMessage message) {
		// TODO Auto-generated method stub
		if(message.isUserMessage() && message.getUserMessage() instanceof OTMessage) {
			OTMessage otm = (OTMessage)message.getUserMessage();
			String tag = ((ListEdit)otm.getMessage()).getList();
			filters.get(tag).filterMessage(message);
		} else {
			messageProcessor.processMessage(message);
		}
	}

	public void setMessageProcessor(MessageProcessor<SentMessage> arg0) {
		// TODO Auto-generated method stub
		messageProcessor = arg0;
		for(OTServerFilter filter : filters.values()) {
			filter.setMessageProcessor(arg0);
		}
	}

	public void userJoined(String arg0, String arg1, String username) {
		// TODO Auto-generated method stub
		for(OTServerFilter filter : filters.values()) {
			filter.userJoined(arg0, arg1, username);
		}
	}

	public void userLeft(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		for(OTServerFilter filter : filters.values()) {
			filter.userLeft(arg0, arg1, arg2);
		}
	}

}
