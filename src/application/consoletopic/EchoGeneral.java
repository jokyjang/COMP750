package application.consoletopic;

import trace.echo.modular.EchoTracerSetter;
import echo.general.ASimpleList;
import echo.general.AnEchoComposerAndLauncher;
import echo.general.AnEchoInteractor;
import echo.general.EchoerInteractor;
import echo.general.SimpleList;
/**
 * This class is to implement an application with Console as view and Topic as model.
 * However, we cannot use EchoerInteractor in the echo.modular package directly as
 * that class is expected to serve only ASimpleList\<String\> while in this case, we
 * want it to serve ASimpleList\<Character\>. So we use echo.general instead.
 * @author zhangzhx
 *
 */

public class EchoGeneral {
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
	protected SimpleList<String> createHistory() {
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
		topic = createHistory();
	}
	
	public SimpleList<String> getHistory() {
		return topic;
	}
	public EchoerInteractor getInteractor() {
		return interactor;
	}
	public static void traceUnawareLaunch(String[] args) {
		(new AnEchoComposerAndLauncher()).composeAndLaunch(args);
	}
	public static void main (String[] args) {
		EchoTracerSetter.traceEchoer();
		// comment out if testing
//		Tracer.showInfo(false); 
//		(new AnEchoComposerAndLauncher()).composeAndLaunch(args);
		traceUnawareLaunch(args);
	}
}
