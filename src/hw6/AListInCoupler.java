package hw6;

import im.ListEdit;
import trace.echo.modular.OperationName;
import trace.im.ListEditReceived;
import util.session.CommunicatorSelector;
import util.session.PeerMessageListener;

public class AListInCoupler<ElementType> implements PeerMessageListener {
	protected SimpleList<ElementType> list;
	public AListInCoupler(SimpleList<ElementType> theEchoer) {
		list = theEchoer;
	}
	public void objectReceived(Object message, String userName) {
		// need for integration with RPC
		if (message instanceof ListEdit)
			processReceivedListEdit((ListEdit<ElementType>) message, userName);
	}
	protected void processReceivedListEdit (ListEdit<ElementType> aRemoteEdit, String aUserName) {
		//if (!aRemoteEdit.getList().equals(ApplicationTags.IM))
		if (!aRemoteEdit.getList().equals(list.getTracingTag()))
			return;
		ListEditReceived.newCase(
				CommunicatorSelector.getProcessName(),
				aRemoteEdit.getOperationName(), 
				aRemoteEdit.getIndex(), 
				aRemoteEdit.getElement(),
				aRemoteEdit.getList(),
				aUserName, 
				this);
		ElementType anInput = aRemoteEdit.getElement();
		// not observable add so we do not get echo message
		// add before we print the message in case some  something reacts to the print
		// before we add
//		history.add(aRemoteEdit.getIndex(), anInput);
		// actually no bounce back should occur should occur
		if(aRemoteEdit.getOperationName() == OperationName.ADD) {
			list.observableAdd(normalizedIndex(list, aRemoteEdit.getIndex()), anInput);
		} else if(aRemoteEdit.getOperationName() == OperationName.DELETE) {
			//list.observableAdd(normalizedIndex(list, aRemoteEdit.getIndex()), anInput);
			list.observableRemove(normalizedIndex(list, aRemoteEdit.getIndex()));
		}

//		System.out.println(IMUtililties.remoteEcho(anInput, aUserName));
	
	}
	protected static int normalizedIndex(SimpleList aTopic, int index) {
		int size = aTopic.size();
		return (index > size || index < 0) ?size: index;
	}

}
