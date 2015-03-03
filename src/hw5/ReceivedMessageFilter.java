package hw5;

import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;

import trace.causal.ConcurrentVectorTimeStampedMessageDetected;
import trace.causal.RemoteCountIncrementedInSiteVectorTimeStamp;
import trace.causal.VectorTimeStampedMessageBuffered;
import trace.causal.VectorTimeStampedMessageDelivered;
import trace.causal.VectorTimeStampedMessageReceived;
import trace.causal.VectorTimeStampedMessageRemovedFromBuffer;
import util.session.MessageFilter;
import util.session.MessageProcessor;
import util.session.ReceivedMessage;

public class ReceivedMessageFilter implements MessageFilter<ReceivedMessage> {
	MessageProcessor<ReceivedMessage> messageProcessor;
	CausalityManager causalityManager;
	PriorityQueue<TimeStampVectorWithMessage> buffer;
	
	public ReceivedMessageFilter(CausalityManager cm) {
		this.causalityManager = cm;
		buffer = new PriorityQueue<TimeStampVectorWithMessage>(13, 
				 new MsgComparator());
	}
	
	/**
	 * To detect if ts2 is concurrent of ts1
	 * @param ts1
	 * @param ts2
	 * @return
	 */
	private String concurrent(Map<String, Integer> ts1, Map<String, Integer> ts2) {
		int bigger = 0, counter = 0;
		String successorUser = null;
		for(String user : ts1.keySet()) {
			if(ts1.get(user) - ts2.get(user) > 1) ++bigger;
			else if(ts1.get(user) - ts2.get(user) == 1) {
				++counter;
				successorUser = user;
			}
		}
		return counter == 1 && bigger == 0 ? successorUser : null;
	}
	
	/**
	 * To detect if ts2 is strict successor to ts1
	 * @param ts1
	 * @param ts2
	 * @return
	 */
	private String successor(Map<String, Integer> ts1, Map<String, Integer> ts2) {
		int equal = 0, counter = 0;
		String successorUser = null;
		for(String user : ts1.keySet()) {
			if(ts1.get(user) - ts2.get(user) == 0) ++equal;
			else if(ts1.get(user) - ts2.get(user) == 1) {
				++counter;
				successorUser = user;
			}
		}
		return counter == 1 && equal == ts1.size()-1 ? successorUser : null;
	}
	
	public void filterMessage(ReceivedMessage msg) {
		// TODO Auto-generated method stub
		//System.out.println("ReceiveMessageFilter.filterMessage: " + msg.toString());
		if(!msg.isUserMessage()) {
			messageProcessor.processMessage(msg);
		} else {
			TimeStampVectorWithMessage message = (TimeStampVectorWithMessage) msg.getUserMessage();
			VectorTimeStampedMessageReceived.newCase(msg, message.getTimeStamp(), this);
			//Collections.sort(buffer, new MsgComparator());
			//buffer.add(message);
			//Map<String, Integer> map = message.getTimeStamp();
			//for (String user : map.keySet()) {
			//	System.out.print("Receive:" + user + ": " + map.get(user)+",");
			//}
			//System.out.println();
			
			if(!causalityManager.isEnabled()) {
				buffer.add(message);
				VectorTimeStampedMessageBuffered.newCase(0, msg, message.getTimeStamp(), this);
				while(!buffer.isEmpty()) {
					message = buffer.poll();
					VectorTimeStampedMessageRemovedFromBuffer.newCase(msg, message.getTimeStamp(), this);
					causalityManager.updateTimeStamp(message.getTimeStamp());
					RemoteCountIncrementedInSiteVectorTimeStamp.newCase(
							causalityManager.getTimeStamps(), this);
					msg.setUserMessage(message.getMessage());
					messageProcessor.processMessage(msg);
					VectorTimeStampedMessageDelivered.newCase(msg, message.getTimeStamp(), this);
				}
			} else {
				String successorUser = this.successor(message.getTimeStamp(), causalityManager.getTimeStamps());
				String concurrentUser = this.concurrent(message.getTimeStamp(), causalityManager.getTimeStamps());
				if (successorUser == null && concurrentUser == null) {
					buffer.add(message);
					VectorTimeStampedMessageBuffered.newCase(0, msg, message.getTimeStamp(), this);
				} else {
					if(concurrentUser != null) {
						ConcurrentVectorTimeStampedMessageDetected.newCase(msg,
								message.getTimeStamp(), this);
						successorUser = concurrentUser;
					}
					causalityManager.increaseCounterByOne(successorUser);
					RemoteCountIncrementedInSiteVectorTimeStamp.newCase(
							causalityManager.getTimeStamps(), this);
					msg.setUserMessage(message.getMessage());
					messageProcessor.processMessage(msg);
					VectorTimeStampedMessageDelivered.newCase(msg,
							message.getTimeStamp(), this);
					while (!buffer.isEmpty()) {
						successorUser = this.successor(buffer.peek()
								.getTimeStamp(), causalityManager
								.getTimeStamps());
						if (successorUser == null) {
							successorUser = this.concurrent(buffer.peek()
									.getTimeStamp(), causalityManager
									.getTimeStamps());
							if (successorUser == null)
								break;
							ConcurrentVectorTimeStampedMessageDetected.newCase(
									msg, message.getTimeStamp(), this);
						}
						message = buffer.poll();
						/*
						 * Map<String, Integer> map = message.getTimeStamp();
						 * for (String user : map.keySet()) {
						 * System.out.print("Buffer:" + user + ": " +
						 * map.get(user)+","); }
						 */
						VectorTimeStampedMessageRemovedFromBuffer.newCase(msg,
								message.getTimeStamp(), this);
						causalityManager.increaseCounterByOne(successorUser);
						RemoteCountIncrementedInSiteVectorTimeStamp.newCase(
								causalityManager.getTimeStamps(), this);
						msg.setUserMessage(message.getMessage());
						messageProcessor.processMessage(msg);
						VectorTimeStampedMessageDelivered.newCase(msg,
								message.getTimeStamp(), this);
					}
				}
			}
		}
	}
	
	class MsgComparator implements Comparator<TimeStampVectorWithMessage>{
		/**
		 * 
		 * @author zhangzhx
		 *
		 */
		public int compare(TimeStampVectorWithMessage o1,
				TimeStampVectorWithMessage o2) {
			// TODO Auto-generated method stub
			Map<String, Integer> m1 = o1.getTimeStamp();
			Map<String, Integer> m2 = o2.getTimeStamp();
			int equal = 0, smaller = 0, bigger = 0;
			for(String user : m1.keySet()) {
				if(m1.get(user) > m2.get(user)) {
					++bigger;
				} else if(m1.get(user) < m2.get(user)) {
					++smaller;
				} else {
					++equal;
				}
			}
			return equal == m1.size() || (bigger > 0 && smaller > 0) ? 0 : 
				bigger == 0 ? -1 : 1;
		}
		
	}

	public void setMessageProcessor(MessageProcessor<ReceivedMessage> msgProcessor) {
		// TODO Auto-generated method stub
		this.messageProcessor = msgProcessor;
	}
}
