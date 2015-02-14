package application;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.TextField;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;

public class IMUI extends JFrame{
	private JTextField topic = null;
	private JTextField status = null;
	private JTextArea history = null;
	private TextField message = null;
	private JTextField awareMessage = null;
	
	private IMUIListener listener = null;

	public IMUI() {
		super("IMView");
		init();
	}
	
	public void setListener(IMUIListener l) {
		listener = l;
		this.topic.getDocument().addDocumentListener(listener.getTopicDocumentListener());
		this.message.addActionListener(listener.getMessageActionListener());
		this.awareMessage.addActionListener(listener.getAwareMessageActionListener());
		this.awareMessage.getDocument().addDocumentListener(
				listener.getAwareMessageDocumentListener());
	}
	
	private void init() {
		this.setSize(10, 100);
		this.topic = new JTextField(20);
		
		this.status = new JTextField(20);
		this.status.setEditable(false);
		this.message = new TextField(20);
		this.awareMessage = new JTextField(20);
		
		this.history = new JTextArea(10, 20);
		this.history.setEditable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(
				new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		
		this.addCompForBorder("Topic", this.topic);
		this.addCompForBorder("Status", this.status);
		this.add(this.history);
		this.addCompForBorder("Aware Message", this.awareMessage);
		this.addCompForBorder("Message", this.message);
		this.pack();
		this.setVisible(true);
	}

	void addCompForBorder(String title, Component component) {
		TitledBorder border = BorderFactory.createTitledBorder(title);
		JPanel comp = new JPanel(new GridLayout(1, 1), false);
		comp.add(component);
		comp.setBorder(border);

		this.add(comp);
	}
	
	public static void main(String[] args) {
		new IMUI();
	}

	public void setMessageText(String string) {
		// TODO Auto-generated method stub
		message.setText(string);
	}
	
	public String getMessageText() {
		return message.getText();
	}

	public void appendToHistory(String aNewValue) {
		// TODO Auto-generated method stub
		history.append(aNewValue);
	}

	public String getAwareMessageText() {
		// TODO Auto-generated method stub
		return awareMessage.getText();
	}
	
	public void setAwareMessageText(String string) {
		// TODO Auto-generated method stub
		awareMessage.setText(string);
	}
	
	public void insertToTopic(int anIndex, Character aValue) {
		String text = topic.getText();
		topic.setText(text.substring(0, anIndex) + aValue + text.substring(anIndex));
	}
	public void removeFromTopic(int anIndex, int to) {
		String text = topic.getText();
		topic.setText(text.substring(0, anIndex) + text.substring(to));
	}
}