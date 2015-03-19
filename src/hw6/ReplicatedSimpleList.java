package hw6;

public interface ReplicatedSimpleList<ElementType> extends
		SimpleList<ElementType> {
	void replicatedAdd(ElementType newVal);
	void replicatedRemove(int index);
	void replicatedAdd(int index, ElementType newVal);
	// void addReplicatingObserver(HistoryObserver<ElementType> anObserver) ;
}
