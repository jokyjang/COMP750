package mergematrix;

import java.util.ArrayList;
import java.util.List;

import trace.echo.ListEditMade;
import trace.echo.modular.ListEditNotified;
import trace.echo.modular.OperationName;
import util.annotations.Tags;
import util.tags.ApplicationTags;
import util.tags.InteractiveTags;
import echo.modular.ListObserver;
import echo.modular.SimpleList;

@Tags({ApplicationTags.HISTORY, InteractiveTags.MODEL})

public class ASimpleList<ElementType> implements SimpleList<ElementType> {
	String tracingTag;
	List<ElementType> simpleList;
	List<ListObserver<ElementType>> observers;
	
	public ASimpleList(String tag) {
		tracingTag = tag;
		simpleList = new ArrayList();
		observers = new ArrayList();
	}
	
	public synchronized void add(ElementType anElement) {
		simpleList.add(simpleList.size(), anElement);
	}
	
	public synchronized void observableAdd(int anIndex, ElementType anElement) {
		add(anIndex, anElement);
		notifyAdd(anIndex, anElement);
	}
	public void notifyAdd(List<ListObserver<ElementType>> observers, int index, ElementType newValue) {
		ListEditNotified.newCase(OperationName.ADD, index, newValue, this.tracingTag, this);
		for (ListObserver<ElementType> observer:observers)
			observer.elementAdded(index, newValue);
	}

	protected void traceAdd(int anIndex, ElementType anElement) {
		ListEditMade.newCase(OperationName.ADD, anIndex,anElement, this.tracingTag, this);
	}
	protected void traceRemove(int anIndex, ElementType anElement) {
		ListEditMade.newCase(OperationName.DELETE, anIndex,anElement, this.tracingTag, this);
	}
	public synchronized void add(int anIndex, ElementType anElement) {
		simpleList.add(anIndex, anElement);
		traceAdd(anIndex, anElement);
	}
	
	public synchronized ElementType remove(int anIndex) {
		ElementType retVal = simpleList.remove(anIndex);
		traceRemove(anIndex, retVal);
		return retVal;
	}
	
	public synchronized boolean remove(ElementType anElement) {
		int anIndex = simpleList.indexOf(anElement);
		if (anIndex < 0)
			return false;
		remove(anIndex);
		return true;		
	}
	
	
	public synchronized boolean observableRemove(ElementType anElement) {
		int anIndex = simpleList.indexOf(anElement);
		if (anIndex < 0)
			return false;
		observableRemove(anIndex);
		return true;
		
	}
	
	public synchronized void observableAdd(ElementType anElement) {
		observableAdd (simpleList.size(), anElement);		
	}

	public synchronized ElementType observableRemove(int anIndex) {
		
		ElementType retVal = remove(anIndex);
		notifyRemove(anIndex, retVal);
		return retVal;
	}
	
	public int size() {
		return simpleList.size() ;
	}
	
	public ElementType get(int index) {
		return simpleList.get(index);
	}
	
	public void addObserver(ListObserver<ElementType> anObserver) {
		observers.add(anObserver);
	}

	public void notifyRemove(List<ListObserver<ElementType>> observers, int index, ElementType newValue) {
		ListEditNotified.newCase(OperationName.DELETE, index, newValue, this.tracingTag, this);
		for (ListObserver<ElementType> observer:observers)
			observer.elementRemoved(index, newValue);
	}
	
	public void notifyAdd(int index, ElementType newValue) {
		notifyAdd(observers, index, newValue);
	}
	
	public void notifyRemove(int index, ElementType newValue) {
		notifyRemove(observers, index, newValue);
	}
	
	public String getTracingTag() {
		return tracingTag;
	}

	@Override
	public void removeObserver(ListObserver<ElementType> anObserver) {
		// TODO Auto-generated method stub
		observers.remove(anObserver);
	}
}
