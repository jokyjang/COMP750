package mergematrix;

public interface ReplicatedSimpleList<ElementType> extends
		SimpleList<ElementType> {
	void replicatedAdd(ElementType newVal);
	void replicatedRemove(int index);
	void replicatedAdd(int index, ElementType newVal);
	String getClientName();
	// void addReplicatingObserver(HistoryObserver<ElementType> anObserver) ;
}
