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
 * @param <ElementType>
 */

public class ASimpleList<ElementType> implements SimpleList<ElementType> {
	List<ElementType> simpleList = new ArrayList();
	List<ListObserver<ElementType>> observers = new ArrayList();

	public synchronized void add(ElementType anElement) {
		simpleList.add(simpleList.size(), anElement);
	}

	public synchronized void observableAdd(int anIndex, ElementType anElement) {
		add(anIndex, anElement);
		notifyAdd(anIndex, anElement);
	}

	public void notifyAdd(List<ListObserver<ElementType>> observers, int index,
			ElementType newValue) {
		ListEditNotified.newCase(OperationName.ADD, index, newValue,
				ApplicationTags.HISTORY, this);
		for (ListObserver<ElementType> observer : observers)
			observer.elementAdded(index, newValue);
	}

	protected void traceAdd(int anIndex, ElementType anElement) {
		ListEditMade.newCase(OperationName.ADD, anIndex, anElement,
				ApplicationTags.HISTORY, this);
	}

	protected void traceRemove(int anIndex, ElementType anElement) {
		ListEditMade.newCase(OperationName.DELETE, anIndex, anElement,
				ApplicationTags.HISTORY, this);
	}

	public synchronized void add(int anIndex, ElementType anElement) {
		simpleList.add(anIndex, anElement);
		traceAdd(anIndex, anElement);
	}

	@Override
	public synchronized ElementType remove(int anIndex) {
		ElementType retVal = simpleList.remove(anIndex);
		traceRemove(anIndex, retVal);
		return retVal;
	}

	@Override
	public synchronized boolean remove(ElementType anElement) {
		int anIndex = simpleList.indexOf(anElement);
		if (anIndex < 0)
			return false;
		remove(anIndex);
		return true;
	}

	@Override
	public synchronized boolean observableRemove(ElementType anElement) {
		int anIndex = simpleList.indexOf(anElement);
		if (anIndex < 0)
			return false;
		observableRemove(anIndex);
		return true;

	}

	public synchronized void observableAdd(ElementType anElement) {

		observableAdd(simpleList.size(), anElement);
	}

	public synchronized ElementType observableRemove(int anIndex) {

		ElementType retVal = remove(anIndex);
		notifyRemove(anIndex, retVal);
		return retVal;
	}

	@Override
	public int size() {
		return simpleList.size();
	}

	@Override
	public ElementType get(int index) {
		return simpleList.get(index);
	}

	@Override
	public void addObserver(ListObserver<ElementType> anObserver) {
		observers.add(anObserver);
	}

	@Override
	public void removeObserver(ListObserver<ElementType> anObserver) {
		observers.remove(anObserver);
	}

	private void notifyRemove(List<ListObserver<ElementType>> observers,
			int index, ElementType newValue) {
		ListEditNotified.newCase(OperationName.DELETE, index, newValue,
				ApplicationTags.HISTORY, this);
		for (ListObserver<ElementType> observer : observers)
			observer.elementRemoved(index, newValue);
	}

	public void notifyAdd(int index, ElementType newValue) {
		notifyAdd(observers, index, newValue);
	}

	public void notifyRemove(int index, ElementType newValue) {
		notifyRemove(observers, index, newValue);
	}
}
