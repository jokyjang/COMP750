package mergematrix;
/**
 * This is an observer monitoring a list if some element has been added or
 * removed.
 * @author zhangzhx
 *
 * @param <ElementType>
 */
public interface ListObserver<ElementType> {
	/**
	 * When an element `aNewValue' is added at the position `anIndex'.
	 * @param anIndex
	 * @param aNewValue
	 */
	void elementAdded(int anIndex, ElementType aNewValue);
	/**
	 * 
	 * @param anIndex
	 * @param aNewValue
	 */
	void elementRemoved(int anIndex, ElementType aNewValue);
	/**
	 * When an element at position `anIndex' is removed from list.
	 * @param anIndex
	 * @return the removed element.
	 */
	//ElementType elementRemoved(int anIndex);
	void elementReplaced(int anIndex, ElementType aNewValue);
}
