package hw5;

import util.session.Communicator;
import util.session.CommunicatorSelector;
import util.session.PeerMessageListener;
import echo.modular.ListObserver;

public class GuiIMComposerAndLauncher {
	protected Communicator histComm;
	protected ReplicatedSimpleList<String> history;
	protected GuiIMInteractor historyInter;
	protected PeerMessageListener historyInCoupler;
	
	public GuiIMComposerAndLauncher(Communicator comm) {
		this.histComm = comm;
	}
	
	public void compose() {
		history = new AReplicatedSimpleList<String>(histComm, ApplicationTags.IM);
		historyInter = new GuiIMInteractor(history, histComm, histComm.getClientName());
		history.addObserver(historyInter);
		historyInCoupler = new AListInCoupler<String>(history);
		histComm.addPeerMessageListener(historyInCoupler);
		//histComm.join();
	}
	
	protected void launchGraphicUI() {
		this.historyInter.doInput();
	}
	
	public void launch() {
		launchGraphicUI();
	}

	public void composeAndLaunch() {
		// TODO Auto-generated method stub
		compose();
		launch();
	}

	public String getApplicationName() {
		// TODO Auto-generated method stub
		return null;
	}

	public void checkArgs(String[] args) {
		// TODO Auto-generated method stub
		if (args.length < 5) {
			System.out.println("Please supply server host name, session name,"
					+ "  user name and application name as main arguments");
			System.exit(-1);
		}
	}

	public Communicator createCommunicator(String[] args) {
		// TODO Auto-generated method stub
		checkArgs(args);
		if (args.length == 5) {
			if (args[4].equalsIgnoreCase(Communicator.DIRECT))
				CommunicatorSelector.selectDirectCommunicator();
			else if (args[4].equalsIgnoreCase(Communicator.RELAYED))
				CommunicatorSelector.selectRelayerCommunicator();
		}
		return CommunicatorSelector.getCommunicator(args[0], args[1], args[2],
				args[3]);
		// CommunicatorCreator communicatorFactory =
		// ACommunicatorSelector.getCommunicatorFactory();
		// return communicatorFactory.getCommunicator(args[0],args[1],args[2],
		// applicationName);
	}
	
	public Communicator getCommunicator() {
		// TODO Auto-generated method stub
		return histComm;
	}

	public PeerMessageListener getRemoteInputEchoer() {
		// TODO Auto-generated method stub
		return historyInCoupler;
	}
	
	public ListObserver getInteractor() {
		return historyInter;
	}
}