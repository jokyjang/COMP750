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
	private OTServerFilter mergeMatrixFilter;
	
	public OTServerFilters() {
		filters = new HashMap<String, OTServerFilter>();
		historyFilter = new OTServerFilter();
		topicFilter = new OTServerFilter();
		mergeMatrixFilter = new OTServerFilter();
		filters.put(ApplicationTags.IM, historyFilter);
		filters.put(ApplicationTags.EDITOR, topicFilter);
		filters.put(ApplicationTags.MERGE_MATRIX, mergeMatrixFilter);
	}

	public void filterMessage(SentMessage message) {
		// TODO Auto-generated method stub
		if(!message.isUserMessage() || !(message.getUserMessage() instanceof OTMessage)) {
			messageProcessor.processMessage(message);
			return;
		}
		OTMessage otm = (OTMessage) message.getUserMessage();
		if(otm.getMessage() instanceof ListEdit) {
			String tag = ((ListEdit) otm.getMessage()).getList();
			filters.get(tag).filterMessage(message);
		} else if(otm.getMessage() instanceof MergePolicyEdit) {
			System.out.println("Server received an MergePolicyEdit!");
			MergePolicyEdit mpe = (MergePolicyEdit) otm.getMessage();
			for(OTServerFilter serverFilter : filters.values()) {
				serverFilter.setMergePolicy(mpe.getServer(), mpe.getClient(), mpe.getPolicy());
			}
			filters.get(ApplicationTags.MERGE_MATRIX).filterMessage(message);
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
