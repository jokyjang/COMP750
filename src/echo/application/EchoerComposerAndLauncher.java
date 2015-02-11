package echo.application;

public abstract class EchoerComposerAndLauncher {
	public abstract void createModel();
	public abstract void createInteractor();
	public abstract void connectModelAndInteractor();
	public void compose(String[] args) {
		createModel();
		createInteractor();
		connectModelAndInteractor();
	}
	public abstract void launch();
	public void composeAndLaunch(String[] args) {
		compose(args);
		launch();
	}
}
