package hw5;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;

import trace.causal.UserAddedToVectorTimeStamp;
import trace.causal.VectorTimeStampCreated;
import util.session.SessionMessageListener;

public class CausalityManager implements SessionMessageListener {
	private boolean enabled;
	private Map<String, Integer> timeStamps;
	public CausalityManager() {
		timeStamps = new Hashtable<String, Integer>();
		VectorTimeStampCreated.newCase(timeStamps, this);
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void enable() {
		enabled = true;
	}
	
	public void disable() {
		enabled = false;
		/*
		for(String user : timeStamps.keySet()) {
			timeStamps.put(user, 0);
		}
		*/
	}

	public void clientJoined(String aClientName, String anApplicationName,
			String aSessionName, boolean isNewSession, boolean isNewApplication,
			Collection<String> allUsers) {
		// TODO Auto-generated method stub
		//System.out.println(aClientName + " client joined");
		timeStamps.put(aClientName, 0);
		UserAddedToVectorTimeStamp.newCase(timeStamps, this);
	}

	public void clientLeft(String aClientName, String anApplicationName) {
		// TODO Auto-generated method stub
		System.out.println("client left!");
	}
	
	public Map<String, Integer> getTimeStamps() {
		return timeStamps;
	}
	
	public void setTimeStamp(Map<String, Integer> ts) {
		this.timeStamps = ts;
	}
	
	public Integer increaseCounterByOne(String key) {
		Integer updatedValue = this.timeStamps.get(key);
		if (updatedValue != null) {
			updatedValue += 1;
			this.timeStamps.put(key, updatedValue);
		}
		return updatedValue;
	}

	/**
	 * This will update current timeStamp setting each counter to whichever is bigger
	 * between this.timeStamp and timeStamp
	 * @param timeStamp
	 */
	public void updateTimeStamp(Map<String, Integer> timeStamp) {
		for(String user : this.timeStamps.keySet()) {
			Integer value = timeStamps.get(user) > timeStamp.get(user) ? timeStamps.get(user) : timeStamp.get(user);
			timeStamps.put(user, value);
		}
	}

}
