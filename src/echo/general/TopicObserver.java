package echo.general;

public class TopicObserver implements ListObserver<Character> {
	private EchoerInteractor echoer;
	
	public TopicObserver(EchoerInteractor e) {
		echoer = e;
	}

	@Override
	public void elementAdded(int anIndex, Character aNewValue) {
		// TODO Auto-generated method stub
		echoer.addedToTopic(anIndex, aNewValue);
	}

	@Override
	public void elementRemoved(int anIndex, Character aNewValue) {
		// TODO Auto-generated method stub
		echoer.removedFromTopic(anIndex, aNewValue);
	}

}
