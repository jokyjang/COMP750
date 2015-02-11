package echo.modular;

import trace.echo.modular.EchoTracerSetter;
import util.annotations.Tags;
import util.tags.ApplicationTags;
import util.tags.InteractiveTags;
/**
 * Single User Single View (in the form of Echo)
 * @author zhangzhx
 *
 */

@Tags({ApplicationTags.ECHOER, InteractiveTags.COMPOSER})
public class AnEchoComposerAndLauncher implements EchoerComposerAndLauncher{
	protected SimpleList<String> history;	
	protected EchoerInteractor interactor;	
	protected void connectModelInteractor() {
		interactor = createInteractor();
		history.addObserver(interactor);
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
		return new AnEchoInteractor(history);
	}	
	public void compose(String[] args) {
		createModel();
//		history = createHistory();
//		interactor = createInteractor();
//		history.addObserver(interactor);
		connectModelInteractor();
	}
	protected void createModel() {
		history = createHistory();
	}
	
	public SimpleList<String> getHistory() {
		return history;
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
