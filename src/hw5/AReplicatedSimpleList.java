package hw5;

import im.AListEdit;
import im.ListEdit;

import java.util.ArrayList;
import java.util.List;

import trace.echo.modular.OperationName;
import trace.im.ListEditSent;
import util.session.Communicator;
import util.trace.session.AddressedSentMessageInfo;
import echo.modular.ListObserver;

public class AReplicatedSimpleList<ElementType> extends ASimpleList<ElementType>
		implements ReplicatedSimpleList<ElementType> {
	Communicator communicator;
	List<ListObserver<ElementType>> replicatingObservers = new ArrayList();

	public AReplicatedSimpleList(Communicator theCommunicator, String theTag) {
		super(theTag);
		communicator = theCommunicator;
	}
	public synchronized void replicatedAdd(ElementType anElement) {
		int anIndex = size();
		replicatedAdd(anIndex, anElement);
	}
	public synchronized void replicatedRemove(int index) {
		ElementType val = super.observableRemove(index);
		if (communicator == null) return;
		ListEdit listEdit = new AListEdit<ElementType>(OperationName.DELETE, index, val, tracingTag);
		ListEditSent.newCase(
				communicator.getClientName(),
				listEdit.getOperationName(), 
				listEdit.getIndex(), 
				listEdit.getElement(), 
				listEdit.getList(),
			AddressedSentMessageInfo.OTHERS, this);
		communicator.toOthers(listEdit);
//		notifyReplicatingObservers(normalizedIndex(anIndex), anElement);
		//notifyReplicatingObservers(anIndex, anElement);
		this.notifyRemove(replicatingObservers, index, val);
	}
	
	public synchronized void replicatedAdd(int anIndex, ElementType anElement) {
		super.observableAdd(anIndex, anElement);
		if (communicator == null) return;
//		communicator.toOthers(new ARemoteInput(input));
//		communicator.toOthers(input);
		ListEdit listEdit = new AListEdit<ElementType>(OperationName.ADD, anIndex, anElement, tracingTag);
		ListEditSent.newCase(
				communicator.getClientName(),
				listEdit.getOperationName(), 
				listEdit.getIndex(), 
				listEdit.getElement(), 
				listEdit.getList(),
			AddressedSentMessageInfo.OTHERS, this);
		communicator.toOthers(listEdit);
//		notifyReplicatingObservers(normalizedIndex(anIndex), anElement);
		notifyReplicatingObservers(anIndex, anElement);



	}
	public void notifyReplicatingObservers(int index, ElementType newValue) {
//		ListEditNotified.newCase(OperationName.ADD, index, newValue, ApplicationTags.IM, this);
//		for (HistoryObserver<ElementType> observer:observers)
//			observer.elementAdded(index, newValue);
		notifyAdd(replicatingObservers, index, newValue);
	}

//	@Override
//	public void addReplicatingObserver(HistoryObserver<ElementType> anObserver) {
//		replicatingObservers.add(anObserver);
//		
//	}
//	
}
