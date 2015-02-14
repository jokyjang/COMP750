package echo.general;
/**
 * This is an observer monitoring a list if some element has been added or
 * removed.
 * @author zhangzhx
 *
 * @param <E>
 */
public interface ListObserver<E> {
	/**
	 * When an element `aNewValue' is added at the position `anIndex'.
	 * @param anIndex
	 * @param aNewValue
	 */
	void elementAdded(int anIndex, E aNewValue);
	/**
	 * 
	 * @param anIndex
	 * @param aNewValue
	 */
	void elementRemoved(int anIndex, E aNewValue);
}
