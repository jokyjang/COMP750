package echo.general;

public interface TopicInteractor {

	public void addedToTopic(int anIndex, Character aNewValue);

	public void removedFromTopic(int anIndex, Character aNewValue);
	
	public void processAddToTopic(int anIndex, Character aNewValue);
	
	public void processRemoveFromTopic(int from, int to);
	
}
