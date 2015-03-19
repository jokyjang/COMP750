package hw6;

import im.AListEdit;
import im.ListEdit;

import java.util.ArrayList;
import java.util.List;

import trace.echo.modular.OperationName;
import trace.im.ListEditSent;
import util.session.Communicator;
import util.trace.session.AddressedSentMessageInfo;
import echo.modular.ListObserver;

public class AReplicatedSimpleList<T> extends ASimpleList<T>
		implements ReplicatedSimpleList<T> {
	Communicator communicator;
	List<ListObserver<T>> replicatingObservers = new ArrayList();

	public AReplicatedSimpleList(Communicator theCommunicator, String theTag) {
		super(theTag);
		communicator = theCommunicator;
	}
	public synchronized void replicatedAdd(T anElement) {
		int anIndex = size();
		replicatedAdd(anIndex, anElement);
	}
	public synchronized void replicatedRemove(int index) {
		T val = super.observableRemove(index);
		if (communicator == null) return;
		ListEdit listEdit = new AListEdit<T>(OperationName.DELETE, index, val, tracingTag);
		ListEditSent.newCase(
				communicator.getClientName(),
				listEdit.getOperationName(), 
				listEdit.getIndex(), 
				listEdit.getElement(), 
				listEdit.getList(),
			AddressedSentMessageInfo.OTHERS, this);
		communicator.toOthers(listEdit);
		this.notifyRemove(replicatingObservers, index, val);
	}
	
	public synchronized void replicatedAdd(int anIndex, T anElement) {
		super.observableAdd(anIndex, anElement);
		if (communicator == null) return;
		ListEdit listEdit = new AListEdit<T>(OperationName.ADD, anIndex, anElement, tracingTag);
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
	public void notifyReplicatingObservers(int index, T newValue) {
		notifyAdd(replicatingObservers, index, newValue);
	}
}
