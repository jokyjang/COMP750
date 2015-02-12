package echo.general;

/**
 * A simple list module design of data structure list.
 * 
 * @author zhangzhx
 *
 * @param <ElementType>
 */

public interface SimpleList<ElementType> {
	
	void add(int index, ElementType input);

	void add(ElementType input);

	void observableAdd(int index, ElementType input);

	void observableAdd(ElementType input);

	int size();

	ElementType get(int index);
	
	ElementType remove(int anIndex);

	boolean remove(ElementType anElement);

	boolean observableRemove(ElementType anElement);
	
	ElementType observableRemove(int idx);
	
	void addObserver(ListObserver<ElementType> anObserver);
	
	void removeObserver(ListObserver<ElementType> anObserver);

}
