package hw5;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import util.session.Communicator;
import util.session.CommunicatorSelector;
import util.session.MessageFilterCreator;
import util.session.ReceivedMessage;
import util.session.ReceivedMessageFilterSelector;
import util.session.SentMessage;
import util.session.SentMessageFilterSelector;
import util.session.SessionMessageListener;

public class GuiComposerAndLauncher {

	private Communicator comm;
	private CausalityManager causalityManager;
	private ParameterSetter ps;

	public void composeAndLaunch(String server, String session, String user,
			String type) {
		String[] args = { server, session, user, ApplicationTags.CAUSALITY, type };
		causalityManager = new CausalityManager();
		this.initSetting();
		comm = this.createCommunicator(args);
		comm.addSessionMessageListener(causalityManager);
		GuiIMComposerAndLauncher imCAL = new GuiIMComposerAndLauncher(comm);
		imCAL.compose();
		GuiEditorComposerAndLauncher editorCAL = new GuiEditorComposerAndLauncher(
				comm);
		editorCAL.compose();
		comm.join();

		ps = new ParameterSetter();
		ps.setVisible(true);
		GUIView viewer = new GUIView((GuiIMInteractor) imCAL.getInteractor(),
				(GuiEditorInteractor) editorCAL.getInteractor());
		viewer.setVisible(true);
		imCAL.launch();
	}

	private void initSetting() {
		// set factories used to create communicator
		MessageFilterCreator<SentMessage> sentMessageQueuerCreator = new CausalitySendFilterCreator(
				(CausalityManager) causalityManager);
		MessageFilterCreator<ReceivedMessage> receivedMessageQueuerCreator = new CausalityReceiveFilterCreator(
				(CausalityManager) causalityManager);
		SentMessageFilterSelector
				.setMessageFilterCreator(sentMessageQueuerCreator);
		ReceivedMessageFilterSelector
				.setMessageFilterCreator(receivedMessageQueuerCreator);
	}

	private void setDelay(String peer, int minDelay, int delayVariation) {
		//System.out.println(peer + ": variation, " + delayVariation + "; minDelay, " + minDelay);
		comm.setMinimumDelayToPeer(peer, minDelay);
		comm.setDelayVariation(delayVariation);
		//P2PDelayAndJitterParameters delayAndJitterParameters = new AP2PDelayAndJitterParameters(
		//		comm, peer);
		//delayAndJitterParameters.setDelayVariation(delayVariation);
		//delayAndJitterParameters.setMinimumDelayToPeer(minDelay);

		// OEFrame aFrame = ObjectEditor.edit(delayAndJitterParameters);
		// aFrame.getFrame().setName("Alice Control");
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

	public SessionMessageListener getSessionAwarenesManager() {
		return causalityManager;
	}

	private class ParameterSetter extends JFrame {
		private String peerName = "";
		private int minDelay = 3000;
		private int delayVariation = 3000;
		private boolean causal = false;

		private JTextField peerField;
		private JTextField minDelayField;
		private JTextField delayVariationField;
		private JButton noJitterButton;
		private JButton noDelayButton;
		private JCheckBox causalCheckBox;

		public ParameterSetter() {
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.getContentPane().setLayout(
					new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));

			this.peerField = new JTextField(peerName, 10);
			this.peerField.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					resetDelay();
				}
			});
			this.minDelayField = new JTextField(Integer.toString(minDelay), 10);
			this.minDelayField.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					resetDelay();
				}

			});
			this.delayVariationField = new JTextField(
					Integer.toString(delayVariation), 10);
			this.delayVariationField.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					resetDelay();
				}

			});

			this.addLabelTextFieldPanel("Peer Name: ", this.peerField);
			this.addLabelTextFieldPanel("Minimum Delay To Peer: ",
					this.minDelayField);
			this.addLabelTextFieldPanel("Delay Variation: ",
					this.delayVariationField);

			this.noJitterButton = new JButton("No Jitter");
			this.noJitterButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					delayVariationField.setText("0");
					resetDelay();
				}

			});

			this.noDelayButton = new JButton("No Delay");
			this.noDelayButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					minDelayField.setText("0");
					resetDelay();
				}

			});
			
			JPanel panel = new JPanel(new FlowLayout());
			panel.add(this.noJitterButton);
			panel.add(this.noDelayButton);
			this.add(panel);
			this.causalCheckBox = new JCheckBox("Causal", this.causal);
			this.causalCheckBox.addChangeListener(new ChangeListener() {

				public void stateChanged(ChangeEvent e) {
					// TODO Auto-generated method stub
					causal = causalCheckBox.isSelected();
					if(causal) {
						causalityManager.enable();
					} else {
						causalityManager.disable();
					}
				}

			});
			this.add(this.causalCheckBox);
			this.pack();
		}
		
		private void resetDelay() {
			peerName = this.peerField.getText();
			minDelay = Integer.parseInt(this.minDelayField.getText());
			delayVariation = Integer.parseInt(this.delayVariationField.getText());
			setDelay(peerName, minDelay, delayVariation);
		}

		public int getMinDelay() {
			return minDelay;
		}

		public int getDelayVariation() {
			return delayVariation;
		}

		public JTextField getPeerField() {
			return peerField;
		}

		private void addLabelTextFieldPanel(String text, JTextField field) {
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(1, 2));
			panel.add(new JLabel(text));
			panel.add(field);
			this.add(panel);
		}
	}
}
