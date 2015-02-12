package application.consoletopic;

import echo.modular.ASimpleList;
import echo.modular.EchoerComposerAndLauncher;
import echo.modular.EchoerInteractor;
import echo.modular.SimpleList;

public class EchoModular implements EchoerComposerAndLauncher{
	protected SimpleList<Character> topic;	
	protected EchoerInteractor interactor;	
	protected void connectModelInteractor() {
		interactor = createInteractor();
		topic.addObserver(interactor);
	}
	public void composeAndLaunch(String[] args) {		
		compose(args);
		launch();
	}
	protected void launchConsoleUI() {
		interactor.doInput();
	}
	public void launch() {
		launchConsoleUI();
	}
	// factory method
	protected SimpleList<Character> createTopic() {
		return new ASimpleList();
	}
	// factory method
	protected EchoerInteractor createInteractor() {
		return new AnEchoInteractor(topic);
	}	
	public void compose(String[] args) {
		createModel();
//		history = createHistory();
//		interactor = createInteractor();
//		history.addObserver(interactor);
		connectModelInteractor();
	}
	protected void createModel() {
		topic = createTopic();
	}
	
	public SimpleList<Character> getTopic() {
		return topic;
	}
	public EchoerInteractor getInteractor() {
		return interactor;
	}
	public static void traceUnawareLaunch(String[] args) {
		(new EchoModular()).composeAndLaunch(args);
	}
	public static void main (String[] args) {
		//EchoTracerSetter.traceEchoer();
		// comment out if testing
//		Tracer.showInfo(false); 
//		(new AnEchoComposerAndLauncher()).composeAndLaunch(args);
		traceUnawareLaunch(args);
	}
	@Override
	public SimpleList<String> getHistory() {
		// TODO Auto-generated method stub
		return null;
	}
}
