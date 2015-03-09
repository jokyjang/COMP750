package mergematrix;

import util.session.Communicator;
import util.session.CommunicatorSelector;
import util.session.PeerMessageListener;
import echo.modular.ListObserver;

public class GuiEditorComposerAndLauncher {
	protected Communicator topicComm;
	protected ReplicatedSimpleList<Character> topic;
	protected ListObserver topicInter;
	protected PeerMessageListener topicInCoupler;
	
	public GuiEditorComposerAndLauncher(Communicator comm) {
		this.topicComm = comm;
	}
	
	public void compose() {
		topic = new AReplicatedSimpleList<Character>(topicComm, ApplicationTags.EDITOR);
		topicInter = new GuiEditorInteractor(topic, topicComm);
		topic.addObserver(topicInter);
		topicInCoupler = new AListInCoupler<Character>(topic);
		topicComm.addPeerMessageListener(topicInCoupler);
		//topicComm.join();
	}
	
	protected void launchGraphicUI() {
		//this.viewer = new GUIView((GuiIMInteractor)this.historyInter,
		//		null);
		//this.viewer.setVisible(true);
		//interactor.doInput();
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

	public PeerMessageListener getRemoteInputEchoer() {
		// TODO Auto-generated method stub
		return topicInCoupler;
	}
	public ListObserver getInteractor() {
		return topicInter;
	}
}