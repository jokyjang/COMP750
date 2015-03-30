package mergematrix;

public interface SimpleList<ElementType> {
	int size();
	ElementType get(int index);
	void addObserver(ListObserver<ElementType> anObserver);	
	void removeObserver(ListObserver<ElementType> anObserver);
	
	void add(int index, ElementType input);
	void add(ElementType input);
	void observableAdd(int index, ElementType input);
	void observableAdd(ElementType input);

	
	ElementType remove(int anIndex);
	boolean remove(ElementType anElement);
	ElementType observableRemove(int index);
	boolean observableRemove(ElementType anElement);
	
	ElementType replace(int anIndex, ElementType input);
	ElementType observableReplace(int anIndex, ElementType input);
	
	String getTracingTag();
}
