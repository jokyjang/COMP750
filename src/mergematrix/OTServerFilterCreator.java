package mergematrix;

import util.session.ServerMessageFilter;
import util.session.ServerMessageFilterCreator;

public class OTServerFilterCreator implements ServerMessageFilterCreator {

	private ServerMessageFilter serverMessageFilter;
	
	public OTServerFilterCreator() {
		this.serverMessageFilter = new OTServerFilters();
	}
	
	public ServerMessageFilter getServerMessageFilter() {
		// TODO Auto-generated method stub
		return this.serverMessageFilter;
	}

}
