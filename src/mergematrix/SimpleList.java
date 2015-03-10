package mergematrix;

import echo.modular.ListObserver;

public interface SimpleList<ElementType> {
	void add(int index, ElementType input);

	void add(ElementType input);

	// void add(ElementType input) ;
	void observableAdd(int index, ElementType input);

	void observableAdd(ElementType input);

	int size();

	ElementType get(int index);

	void addObserver(ListObserver<ElementType> anObserver);
	
	void removeObserver(ListObserver<ElementType> anObserver);

	ElementType remove(int anIndex);

	boolean remove(ElementType anElement);

	ElementType observableRemove(int index);
	
	boolean observableRemove(ElementType anElement);
	
	String getTracingTag();

}
