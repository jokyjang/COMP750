package hw6;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

public class GUIView extends JFrame{
	//private HistoryModel hm;
	//private final String userName;
	private JTextField topic = null;
	private JTextField status = null;
	private JTextArea history = null;
	private JTextField message = null;
	private JTextField awareMessage = null;
	private GuiIMInteractor IMinter = null;
	private GuiEditorInteractor editorInter = null;
	
	public GUIView(GuiIMInteractor imListener, GuiEditorInteractor editorListener) {
		super("IMView");
		this.editorInter = editorListener;
		this.IMinter = imListener;
		if(this.editorInter != null) this.editorInter.setGUI(this);
		if(this.IMinter != null) this.IMinter.setGUI(this);

		this.setSize(10, 100);
		this.topic = new JTextField(20);
		this.topic.getDocument().addDocumentListener(this.editorInter);
		
		this.status = new JTextField(20);
		this.status.setEditable(false);
		this.message = new JTextField(20);
		this.message.addActionListener(this.IMinter);
		this.awareMessage = new JTextField(20);
		this.awareMessage.addActionListener(this.IMinter);
		//AwareDocumentListener adl = new AwareDocumentListener();
		//adl.start();
		this.IMinter.start();
		this.awareMessage.getDocument().addDocumentListener(this.IMinter);
		
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
}