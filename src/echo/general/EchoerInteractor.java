package echo.general;

public interface EchoerInteractor {
	
	void processAddToHistory(String aNewValue);
	
	void processAddToTopic(int anIndex, Character aNewValue);
	
	void processRemoveFromTopic(int from, int to);
	
	void addedToHistory(int anIndex, String aNewValue);

	void addedToTopic(int anIndex, Character aNewValue);

	void removedFromTopic(int anIndex, Character aNewValue);
	
	void start();

}