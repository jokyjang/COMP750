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
		
		MessageFilterCreator<SentMessage> sentMessageQueuerCreator = 
				new OTSendFilterCreator(otManagers);
		MessageFilterCreator<ReceivedMessage> receivedMessageQueuerCreator = 
				new OTReceiveFilterCreator(otManagers);
		SentMessageFilterSelector
				.setMessageFilterCreator(sentMessageQueuerCreator);
		ReceivedMessageFilterSelector
				.setMessageFilterCreator(receivedMessageQueuerCreator);
		
		comm = this.createCommunicator(args);
		GUIView viewer = new GUIView();
		composeIM(viewer);
		composeEditor(viewer);
		comm.join();

		viewer.setVisible(true);
		ps = new ParameterSetter();
		for(OTManager otManager : otManagers.values()) {
			otManager.setMergeMatrix(ps.getMergeMatrix());
		}
		ps.setVisible(true);	
	}
	
	private void composeIM(GUIView view) {
		ReplicatedSimpleList<String> history = 
				new AReplicatedSimpleList<String>(comm, ApplicationTags.IM);
		GuiIMInteractor historyInter = new GuiIMInteractor(history);
		history.addObserver(historyInter);
		PeerMessageListener historyInCoupler = new AListInCoupler<String>(history);
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
		PeerMessageListener topicInCoupler = new AListInCoupler<Character>(topic);
		comm.addPeerMessageListener(topicInCoupler);
		view.setEditInter(topicInter);
		topicInter.setGUI(view);
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
	
	private class MyTableModel extends AbstractTableModel implements TableModelListener{
		private MergeMatrix mergeMatrix = null;
		OperationName[] operations = null;
		Object[][] policyList = null;
		
		public MyTableModel(MergeMatrix aMergeMatrix) {
			super();
			mergeMatrix = aMergeMatrix;
			operations = mergeMatrix.getAllOperations();
			
			policyList = new Object[operations.length][operations.length];
			for(int i = 0; i < operations.length; ++i) {
				for(int j = 0; j < operations.length; ++j) {
					policyList[i][j] = mergeMatrix.get(operations[i], operations[j]);
				}
			}
			this.addTableModelListener(this);
		}
	    public int getColumnCount() {
	        return operations.length + 1;
	    }

	    public int getRowCount() {
	        return operations.length;
	    }

	    public String getColumnName(int col) {
	    	return col == 0 ? "MergeMatrix" : operations[col-1].toString();
	    }

	    public Class getColumnClass(int c) {
	        return getValueAt(0, c).getClass();
	    }

	    public boolean isCellEditable(int row, int col) {
	    	return true;
	    }
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			// TODO Auto-generated method stub
			return columnIndex > 0 ? policyList[rowIndex][columnIndex-1] : operations[rowIndex];
		}
		
		/*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {    
            policyList[row][col - 1] = value;
            fireTableCellUpdated(row, col);
        }
		@Override
		public void tableChanged(TableModelEvent e) {
			// TODO Auto-generated method stub
			int row = e.getFirstRow();
			int col = e.getColumn();
			mergeMatrix.set(operations[row], operations[col-1], (MergePolicy)getValueAt(row, col));
			//mergeMatrix.print();
		}
	}

	private class ParameterSetter extends JFrame {
		private int minDelay = 0;
		private int delayVariation = 0;
		private boolean otable = true;
		private MergeMatrix mergePolicy = null;

		private JTextField minDelayField;
		private JTextField delayVariationField;
		private JButton noJitterButton;
		private JButton noDelayButton;
		private JCheckBox otCheckBox;

		public ParameterSetter() {
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.getContentPane().setLayout(
					new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
			this.initDelay();
			this.initMergeMatrix();
			this.pack();
		}
		
		public MergeMatrix getMergeMatrix() {
			// TODO Auto-generated method stub
			return this.mergePolicy;
		}

		private void initDelay() {
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
			panel.add(this.noJitterButton);
			panel.add(this.noDelayButton);
			panel.add(this.otCheckBox);
			this.add(panel);
		}
		
		private void initMergeMatrix() {
			this.mergePolicy = new SimpleListMergeMatrix();
			MyTableModel tableModel = new MyTableModel(mergePolicy);
			JTable table = new JTable(tableModel);
			for(int i = 1; i < tableModel.getColumnCount(); ++i) {
				JComboBox policyList = new JComboBox(mergePolicy.getAllPolicies());
				table.getColumnModel().getColumn(i).setCellEditor(
						new DefaultCellEditor(policyList));
				//Set up tool tips for the sport cells.
		        DefaultTableCellRenderer renderer =
		                new DefaultTableCellRenderer();
		        renderer.setToolTipText("Click for combo box");
		        table.getColumnModel().getColumn(i).setCellRenderer(renderer);
			}
			table.setCellSelectionEnabled(true);
			table.setRowSelectionAllowed(false);
			table.setColumnSelectionAllowed(false);
			
			JScrollPane scrollPane = new JScrollPane(table);
			table.setFillsViewportHeight(true);
			this.add(scrollPane);
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
