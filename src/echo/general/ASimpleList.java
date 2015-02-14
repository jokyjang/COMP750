package echo.general;
import java.util.ArrayList;
import java.util.List;

import trace.echo.ListEditMade;
import trace.echo.modular.ListEditNotified;
import trace.echo.modular.OperationName;
import util.tags.ApplicationTags;
/**
 * This class is to implement a history model.
 * @author zhangzhx
 *
 * @param <E>
 */

public class ASimpleList<E> implements SimpleList<E> {
	List<E> simpleList = new ArrayList();
	List<ListObserver<E>> observers = new ArrayList();
	String tag = null;
	
	public ASimpleList(String t) {
		tag = t;
	}
	
	public String getTag() {
		return tag;
	}

	public synchronized void add(E anElement) {
		add(simpleList.size(), anElement);
	}

	public synchronized void add(int anIndex, E anElement) {
		simpleList.add(anIndex, anElement);
		traceAdd(anIndex, anElement);
	}

	protected void traceAdd(int anIndex, E anElement) {
		ListEditMade.newCase(OperationName.ADD, anIndex, anElement,
				ApplicationTags.HISTORY, this);
	}

	public synchronized void observableAdd(E anElement) {
		observableAdd(simpleList.size(), anElement);
	}
	
	public synchronized void observableAdd(int anIndex, E anElement) {
		add(anIndex, anElement);
		notifyAdd(anIndex, anElement);
	}

	protected void notifyAdd(int index, E newValue) {
		notifyAdd(observers, index, newValue);
	}

	protected void notifyAdd(List<ListObserver<E>> observers, int index,
			E newValue) {
		ListEditNotified.newCase(OperationName.ADD, index, newValue,
				ApplicationTags.HISTORY, this);
		for (ListObserver<E> observer : observers)
			observer.elementAdded(index, newValue);
	}

	protected void traceRemove(int anIndex, E anElement) {
		ListEditMade.newCase(OperationName.DELETE, anIndex, anElement,
				ApplicationTags.HISTORY, this);
	}

	@Override
	public synchronized E remove(int anIndex) {
		E retVal = simpleList.remove(anIndex);
		traceRemove(anIndex, retVal);
		return retVal;
	}

	@Override
	public synchronized boolean remove(E anElement) {
		int anIndex = simpleList.indexOf(anElement);
		if (anIndex < 0)
			return false;
		remove(anIndex);
		return true;
	}

	@Override
	public synchronized boolean observableRemove(E anElement) {
		int anIndex = simpleList.indexOf(anElement);
		if (anIndex < 0)
			return false;
		observableRemove(anIndex);
		return true;

	}

	public synchronized E observableRemove(int anIndex) {

		E retVal = remove(anIndex);
		notifyRemove(anIndex, retVal);
		return retVal;
	}

	@Override
	public int size() {
		return simpleList.size();
	}

	@Override
	public E get(int index) {
		return simpleList.get(index);
	}

	@Override
	public void addObserver(ListObserver<E> anObserver) {
		observers.add(anObserver);
	}

	@Override
	public void removeObserver(ListObserver<E> anObserver) {
		observers.remove(anObserver);
	}

	private void notifyRemove(List<ListObserver<E>> observers,
			int index, E newValue) {
		ListEditNotified.newCase(OperationName.DELETE, index, newValue,
				ApplicationTags.HISTORY, this);
		for (ListObserver<E> observer : observers)
			observer.elementRemoved(index, newValue);
	}

	public void notifyRemove(int index, E newValue) {
		notifyRemove(observers, index, newValue);
	}
}
