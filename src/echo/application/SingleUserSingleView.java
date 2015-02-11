package echo.application;

import echo.modular.ASimpleList;
import echo.modular.AnEchoInteractor;
import echo.modular.EchoerInteractor;
import echo.modular.SimpleList;

/**
 * This class is to use the framework of echo.modular to implement a 
 * single-user-single-view application.
 * @author zhangzhx
 *
 */
public class SingleUserSingleView extends EchoerComposerAndLauncher{
	private SimpleList<String> history;
	private EchoerInteractor echoer;
	@Override
	public void createModel() {
		// TODO Auto-generated method stub
		history = new ASimpleList<String>();
	}

	@Override
	public void createInteractor() {
		// TODO Auto-generated method stub
		echoer = new AnEchoInteractor(history);
	}

	@Override
	public void connectModelAndInteractor() {
		// TODO Auto-generated method stub
		history.addObserver(echoer);
	}

	@Override
	public void launch() {
		// TODO Auto-generated method stub
		echoer.doInput();
	}
	
	public static void main(String[] args) {
		(new SingleUserSingleView()).composeAndLaunch(args);
	}
}
