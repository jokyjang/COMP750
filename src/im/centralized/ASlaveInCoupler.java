package im.centralized;

import im.AHistoryInCoupler;
import im.ListEdit;
import echo.modular.SimpleList;

public class ASlaveInCoupler extends AHistoryInCoupler {
	// History<String> history;
	String localUserName;

	public ASlaveInCoupler(SimpleList<String> theEchoer, String aUserName) {
		super(theEchoer);
		localUserName = aUserName;
	}

	@Override
	protected void processReceivedListEdit(ListEdit<String> aRemoteEdit,
			String aUserName) {
		if (!(aRemoteEdit instanceof UserEdit))
			return;
		String anInputterName = ((UserEdit<String>) aRemoteEdit).getUserName();
		if (localUserName.equals(anInputterName)) {
			history.observableAdd(aRemoteEdit.getIndex(),
					aRemoteEdit.getElement());
		} else {
			super.processReceivedListEdit(aRemoteEdit, anInputterName);
		}

	}

}
