package echo.general;

/**
 * A simple list module design of data structure list.
 * 
 * @author zhangzhx
 *
 * @param <E>
 */

public interface SimpleList<E> {
	
	String getTag();	// identification for each model
	
	void add(int index, E input);

	void add(E input);

	void observableAdd(int index, E input);

	void observableAdd(E input);

	int size();

	E get(int index);
	
	E remove(int anIndex);

	boolean remove(E anElement);

	E observableRemove(int idx);

	boolean observableRemove(E anElement);
	
	void addObserver(ListObserver<E> anObserver);
	
	void removeObserver(ListObserver<E> anObserver);
}
