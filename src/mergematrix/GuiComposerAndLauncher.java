package mergematrix;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import trace.echo.modular.OperationName;
import util.session.Communicator;
import util.session.CommunicatorSelector;
import util.session.MessageFilterCreator;
import util.session.PeerMessageListener;
import util.session.ReceivedMessage;
import util.session.ReceivedMessageFilterSelector;
import util.session.SentMessage;
import util.session.SentMessageFilterSelector;

public class GuiComposerAndLauncher {
	private Communicator comm;
	private Map<String, OTManager> otManagers;
	private ParameterSetter ps;

	public void composeAndLaunch(String server, String session, String user,
			String type) {
		String[] args = { server, session, user, ApplicationTags.OT, type };
		otManagers = new HashMap<String, OTManager>();
		otManagers.put(ApplicationTags.IM, new OTManager(user, false));
		otManagers.put(ApplicationTags.EDITOR, new OTManager(user, false));
		otManagers.put(ApplicationTags.MERGE_MATRIX, new OTManager(user, false));
		
		MessageFilterCreator<SentMessage> sentMessageQueuerCreator = 
				new OTSendFilterCreator(otManagers);
		MessageFilterCreator<ReceivedMessage> receivedMessageQueuerCreator = 
				new OTReceiveFilterCreator(otManagers);
		SentMessageFilterSelector
				.setMessageFilterCreator(sentMessageQueuerCreator);
		ReceivedMessageFilterSelector
				.setMessageFilterCreator(receivedMessageQueuerCreator);
		
		comm = this.createCommunicator(args);
		ps = new ParameterSetter(comm, otManagers);
		for(OTManager otManager : otManagers.values()) {
			otManager.setMergeMatrix(ps.getMergeMatrix());
		}
		GUIView viewer = new GUIView();
		composeIM(viewer);
		composeEditor(viewer);
		comm.join();

		viewer.setVisible(true);
		ps.setVisible(true);	
	}
	
	private void composeIM(GUIView view) {
		ReplicatedSimpleList<String> history = 
				new AReplicatedSimpleList<String>(comm, ApplicationTags.IM);
		GuiIMInteractor historyInter = new GuiIMInteractor(history);
		history.addObserver(historyInter);
		PeerMessageListener historyInCoupler = new AListInCoupler<String>(
				history, ps);
		comm.addPeerMessageListener(historyInCoupler);
		view.setIMInter(historyInter);
		historyInter.setGUI(view);
		historyInter.start();
	}
	
	private void composeEditor(GUIView view) {
		ReplicatedSimpleList<Character> topic = 
				new AReplicatedSimpleList<Character>(comm, ApplicationTags.EDITOR);
		GuiEditorInteractor topicInter = new GuiEditorInteractor(topic);
		topic.addObserver(topicInter);
		PeerMessageListener topicInCoupler = new AListInCoupler<Character>(
				topic, ps);
		comm.addPeerMessageListener(topicInCoupler);
		view.setEditInter(topicInter);
		topicInter.setGUI(view);
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
	}
}
