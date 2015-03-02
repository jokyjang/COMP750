package echo.general;

public interface HistoryInteractor {

	void processAddToHistory(String aNewValue);
	
	void addedToHistory(int anIndex, String aNewValue);
	
}
