package application.uihistory;

import application.Tags;
import echo.general.ASimpleList;
import echo.general.EchoerComposerAndLauncher;
import echo.general.HistoryEchoerInteractor;
import echo.general.HistoryObserver;
import echo.general.SimpleList;

public class EchoGeneral extends EchoerComposerAndLauncher {
	protected SimpleList<String> history;
	protected HistoryEchoerInteractor historyEchoer;

	@Override
	public void createModel() {
		// TODO Auto-generated method stub
		history = new ASimpleList<String>(Tags.HISTORY);
	}

	@Override
	public void createInteractor() {
		// TODO Auto-generated method stub
		historyEchoer = new AnHistoryUIInteractor(history);
	}

	@Override
	public void connectModelAndInteractor() {
		// TODO Auto-generated method stub
		history.addObserver(new HistoryObserver(historyEchoer));
	}

	@Override
	public void launch() {
		// TODO Auto-generated method stub
		historyEchoer.start();
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

}
