package echo.general;

public class TopicObserver implements ListObserver<Character> {
	private TopicInteractor topic;
	
	public TopicObserver(TopicInteractor e) {
		topic = e;
	}

	@Override
	public void elementAdded(int anIndex, Character aNewValue) {
		// TODO Auto-generated method stub
		topic.addedToTopic(anIndex, aNewValue);
	}

	@Override
	public void elementRemoved(int anIndex, Character aNewValue) {
		// TODO Auto-generated method stub
		topic.removedFromTopic(anIndex, aNewValue);
	}
}
