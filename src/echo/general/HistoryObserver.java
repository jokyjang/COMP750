package echo.general;

public class HistoryObserver implements ListObserver<String> {
	private HistoryInteractor history;
	
	public HistoryObserver(HistoryInteractor e) {
		history = e;
	}

	@Override
	public void elementAdded(int anIndex, String aNewValue) {
		// TODO Auto-generated method stub
		history.addedToHistory(anIndex, aNewValue);
	}

	@Override
	public void elementRemoved(int anIndex, String aNewValue) {
		// TODO Auto-generated method stub
		// DO NOTHING, BECAUSE HISTORY CANNOT BE REMOVED.
	}

}
