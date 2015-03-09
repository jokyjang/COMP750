package mergematrix;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

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
	private Map<String, OTManager> otManagers;
	private ParameterSetter ps;

	public void composeAndLaunch(String server, String session, String user,
			String type) {
		String[] args = { server, session, user, ApplicationTags.OT, type };
		otManagers = new HashMap<String, OTManager>();
		otManagers.put(ApplicationTags.IM, new OTManager(user, false));
		otManagers.put(ApplicationTags.EDITOR, new OTManager(user, false));
		
		MessageFilterCreator<SentMessage> sentMessageQueuerCreator = 
				new OTSendFilterCreator(otManagers);
		MessageFilterCreator<ReceivedMessage> receivedMessageQueuerCreator = 
				new OTReceiveFilterCreator(otManagers);
		SentMessageFilterSelector
				.setMessageFilterCreator(sentMessageQueuerCreator);
		ReceivedMessageFilterSelector
				.setMessageFilterCreator(receivedMessageQueuerCreator);
		
		comm = this.createCommunicator(args);
		
		//comm.addSessionMessageListener(otManager);
		GuiIMComposerAndLauncher imCAL = 
				new GuiIMComposerAndLauncher(comm);
		imCAL.compose();
		GuiEditorComposerAndLauncher editorCAL = 
				new GuiEditorComposerAndLauncher(comm);
		editorCAL.compose();
		comm.join();

		ps = new ParameterSetter();
		ps.setVisible(true);
		GUIView viewer = new GUIView((GuiIMInteractor) imCAL.getInteractor(),
				(GuiEditorInteractor) editorCAL.getInteractor());
		viewer.setVisible(true);
		imCAL.launch();
	}

	private void setDelay(int minDelay, int delayVariation) {
		comm.setMinimumDelayToServer(minDelay);
		comm.setDelayVariation(delayVariation);
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

	/*
	public SessionMessageListener getSessionAwarenesManager() {
		return otManagers;
	}
	*/

	private class ParameterSetter extends JFrame {
		private int minDelay = 0;
		private int delayVariation = 0;
		private boolean otable = false;

		private JTextField minDelayField;
		private JTextField delayVariationField;
		private JButton noJitterButton;
		private JButton noDelayButton;
		private JCheckBox otCheckBox;

		public ParameterSetter() {
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.getContentPane().setLayout(
					new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));

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

			this.addLabelTextFieldPanel("Minimum Delay To Server: ",
					this.minDelayField);
			this.addLabelTextFieldPanel("Delay Variation To Server: ",
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
			this.otCheckBox = new JCheckBox("Apply Operation Transformation", this.otable);
			this.otCheckBox.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					// TODO Auto-generated method stub
					otable = otCheckBox.isSelected();
					if(otable) {
						for(OTManager otManager : otManagers.values())
							otManager.enable();
					} else {
						for(OTManager otManager : otManagers.values())
							otManager.disable();
					}
				}

			});
			this.add(this.otCheckBox);
			this.pack();
		}
		
		private void resetDelay() {
			minDelay = Integer.parseInt(this.minDelayField.getText());
			delayVariation = Integer.parseInt(this.delayVariationField.getText());
			setDelay(minDelay, delayVariation);
		}

		public int getMinDelay() {
			return minDelay;
		}

		public int getDelayVariation() {
			return delayVariation;
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
