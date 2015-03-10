package mergematrix;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class GUIView extends JFrame{
	private JTextField topic = null;
	private JTextField status = null;
	private JTextArea history = null;
	private JTextField message = null;
	private JTextField awareMessage = null;
	private GuiIMInteractor IMinter = null;
	private GuiEditorInteractor editorInter = null;
	
	public GUIView(GuiIMInteractor imListener, GuiEditorInteractor editorListener) {
		this();
		this.setIMInter(imListener);
		this.setEditInter(editorListener);
	}
	
	public GUIView() {
		super("IMView");

		this.setSize(10, 100);
		this.topic = new JTextField(20);
		this.status = new JTextField(20);
		this.status.setEditable(false);
		this.message = new JTextField(20);
		this.awareMessage = new JTextField(20);
		//AwareDocumentListener adl = new AwareDocumentListener();
		//adl.start();
		//this.IMinter.start();
		
		this.history = new JTextArea(10, 20);
		this.history.setEditable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(
				new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		
		this.addCompForBorder("Topic", this.topic);
		this.addCompForBorder("Status", this.status);
		JScrollPane scrollPane = new JScrollPane(this.history);
		scrollPane.setAutoscrolls(true);
		this.add(scrollPane);
		JButton clearOff = new JButton("clear history");
		clearOff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				history.setText("");
			}
		});
		this.add(clearOff);
		this.addCompForBorder("Aware Message", this.awareMessage);
		this.addCompForBorder("Message", this.message);
		this.pack();
	}
	
	public void setIMInter(GuiIMInteractor anIMInteractor) {
		this.IMinter = anIMInteractor;
		this.message.addActionListener(this.IMinter);
		this.awareMessage.addActionListener(this.IMinter);
		this.awareMessage.getDocument().addDocumentListener(this.IMinter);
	}
	
	public void setEditInter(GuiEditorInteractor anEditorInteractor) {
		this.editorInter = anEditorInteractor;
		this.topic.getDocument().addDocumentListener(this.editorInter);
	}

	void addCompForBorder(String title, Component component) {
		TitledBorder border = BorderFactory.createTitledBorder(title);
		JPanel comp = new JPanel(new GridLayout(1, 1), false);
		comp.add(component);
		comp.setBorder(border);

		this.add(comp);
	}

	public JTextField getMessagewidget() {
		// TODO Auto-generated method stub
		return this.message;
	}

	public JTextField getAwareMessageWidget() {
		// TODO Auto-generated method stub
		return this.awareMessage;
	}

	public void appendHistory(String string) {
		// TODO Auto-generated method stub
		this.history.append(string);
	}
	
	public void resetHistory(String aNewHistory) {
		this.history.setText(aNewHistory);
	}

	public void setTopic(String newValue) {
		// TODO Auto-generated method stub
		this.topic.setText(newValue);
	}

	public String getTopic() {
		// TODO Auto-generated method stub
		return this.topic.getText();
	}

	public void setStatus(String string) {
		// TODO Auto-generated method stub
		this.status.setText(string);
	}

	public String getStatus() {
		// TODO Auto-generated method stub
		return this.status.getText();
	}
	
	public static void main(String[] args) {
		new GUIView().setVisible(true);
	}
}