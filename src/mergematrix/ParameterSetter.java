package mergematrix;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class ParameterSetter extends JFrame {
	private int minDelay = 0;
	private int delayVariation = 0;
	private boolean otable = true;
	private MergeMatrix mergePolicy = null;
	private MyTableModel tableModel = null;
	
	private Communicator comm;
	private Map<String, OTManager> otManagers;

	private JTextField minDelayField;
	private JTextField delayVariationField;
	private JButton noJitterButton;
	private JButton noDelayButton;
	private JCheckBox otCheckBox;

	public ParameterSetter(Communicator c, Map<String, OTManager> otm) {
		comm = c;
		otManagers = otm;
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
		this.tableModel = new MyTableModel(mergePolicy);
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
		//table.setFillsViewportHeight(false);
		scrollPane.setPreferredSize(getMinimumSize());
		this.add(scrollPane);
	}
	
	private void resetDelay() {
		minDelay = Integer.parseInt(this.minDelayField.getText());
		delayVariation = Integer.parseInt(this.delayVariationField.getText());
		comm.setMinimumDelayToServer(minDelay);
		comm.setDelayVariation(delayVariation);
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
	
	public void setMergePolicy(OperationName server, OperationName client,
			MergePolicy policy) {
		// TODO Auto-generated method stub
		tableModel.setValueAtFromRemote(policy, server, client);
	}
	
	private class MyTableModel extends AbstractTableModel {
		private MergeMatrix mergeMatrix = null;
		OperationName[] operations = null;
		boolean sentFromRemote = false;
		
		public MyTableModel(MergeMatrix aMergeMatrix) {
			super();
			mergeMatrix = aMergeMatrix;
			operations = mergeMatrix.getAllOperations();
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
			if(columnIndex == 0) return operations[rowIndex];
			OperationName a = operations[rowIndex];
			OperationName b = operations[columnIndex - 1];
			return mergeMatrix.get(a, b);
		}
		
		/*
	     * Don't need to implement this method unless your table's
	     * data can change.
	     */
	    public void setValueAt(Object value, int row, int col) {
			mergeMatrix.set(operations[row], operations[col-1], (MergePolicy) value);
			
			//System.out.println("set Value at " + row + ", " + col + ": " + value);
			//mergeMatrix.print();
			
			this.fireTableCellUpdated(row, col);
			if(this.sentFromRemote) return;
			MergePolicyEdit mergePolicyEdit = new MergePolicyEdit(
					operations[row], operations[col-1], (MergePolicy)getValueAt(row, col));
			comm.toOthers(mergePolicyEdit);
	    }
	    public void setValueAtFromRemote(Object value, OperationName a, OperationName b) {
	    	int row, col;
	    	for(row = 0; row < this.getRowCount(); ++row) {
	    		if(operations[row].equals(a)) {
	    			break;
	    		}
	    	}
	    	for(col = 1; col < this.getColumnCount(); ++col) {
	    		if(operations[col - 1].equals(b)) {
	    			break;
	    		}
	    	}
	    	this.sentFromRemote = true;
	    	setValueAt(value, row, col);
	    	this.sentFromRemote = false;
	    }
	}
}