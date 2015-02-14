package echo.general;

public class HistoryObserver implements ListObserver<String> {
	private EchoerInteractor echoer;
	
	public HistoryObserver(EchoerInteractor e) {
		echoer = e;
	}

	@Override
	public void elementAdded(int anIndex, String aNewValue) {
		// TODO Auto-generated method stub
		echoer.addedToHistory(anIndex, aNewValue);
	}

	@Override
	public void elementRemoved(int anIndex, String aNewValue) {
		// TODO Auto-generated method stub
		// DO NOTHING, BECAUSE HISTORY CANNOT BE REMOVED.
	}

}
