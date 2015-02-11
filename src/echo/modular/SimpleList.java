package echo.modular;

/**
 * A simple list module design of data structure list.
 * 
 * @author zhangzhx
 *
 * @param <ElementType>
 */

public interface SimpleList<ElementType> {
	/**
	 * Insert element `input' at the position `index'
	 * @param index
	 * @param input
	 */
	void add(int index, ElementType input);

	void add(ElementType input);

	/**
	 * Insert element `input' at the position `index' and notify
	 * all the observers.
	 * @param index
	 * @param input
	 */
	void observableAdd(int index, ElementType input);

	void observableAdd(ElementType input);

	int size();

	ElementType get(int index);
	
	ElementType remove(int anIndex);

	boolean remove(ElementType anElement);

	boolean observableRemove(ElementType anElement);
	/**
	 * Register observer `anObserver' to this list.
	 * @param anObserver
	 */
	void addObserver(ListObserver<ElementType> anObserver);
	/**
	 * Remove observer `anObserver' from the list.
	 * @param anObserver
	 */
	void removeObserver(ListObserver<ElementType> anObserver);

}
