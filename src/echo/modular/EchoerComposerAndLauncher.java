package echo.modular;

public interface EchoerComposerAndLauncher {
	public SimpleList<String> getHistory();
	public EchoerInteractor getInteractor();
	public void compose(String[] args);
	public void composeAndLaunch(String[] args);
	public void launch();

}
