package application.consoletopic;

import trace.echo.modular.EchoTracerSetter;
import echo.general.ASimpleList;
import echo.general.AnConsoleInteractor;
import echo.general.EchoerComposerAndLauncher;
import echo.general.EchoerInteractor;
import echo.general.HistoryObserver;
import echo.general.SimpleList;
import echo.general.TopicObserver;
/**
 * This class is to implement an application with Console as view and Topic as model.
 * However, we cannot use EchoerInteractor in the echo.modular package directly as
 * that class is expected to serve only ASimpleList\<String\> while in this case, we
 * want it to serve ASimpleList\<Character\>. So we use echo.general instead.
 * @author zhangzhx
 *
 */

public class EchoGeneral extends EchoerComposerAndLauncher {
	protected SimpleList<Character> topic;
	protected SimpleList<String> history;
	protected EchoerInteractor interactor;
	
	protected void launchConsoleUI() {
		interactor.start();
	}
	public void launch() {
		launchConsoleUI();
	}
	// factory method
	public void createInteractor() {
		interactor = new AnConsoleInteractor(topic, history);
	}	
	
	public void createModel() {
		topic = new ASimpleList<Character>("TOPIC");
		history = new ASimpleList<String>("HISTORY");
	}
	
	public SimpleList<String> getHistory() {
		return history;
	}
	public SimpleList<Character> getTopic() {
		return topic;
	}
	public EchoerInteractor getInteractor() {
		return interactor;
	}
	public static void traceUnawareLaunch(String[] args) {
		(new EchoGeneral()).composeAndLaunch(args);
	}
	public static void main (String[] args) {
		//EchoTracerSetter.traceEchoer();
		// comment out if testing
//		Tracer.showInfo(false); 
//		(new AnEchoComposerAndLauncher()).composeAndLaunch(args);
		traceUnawareLaunch(args);
	}
	@Override
	public void connectModelAndInteractor() {
		// TODO Auto-generated method stub
		topic.addObserver(new TopicObserver(interactor));
		history.addObserver(new HistoryObserver(interactor));
	}
}
