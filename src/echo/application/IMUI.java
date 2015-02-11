package echo.application;
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

public class IMUI extends JFrame{
	private JTextField topic = null;
	private JTextField status = null;
	private JTextArea history = null;
	private TextField message = null;
	private JTextField awareMessage = null;

	public IMUI() {
		super("IMView");

		this.setSize(10, 100);
		this.topic = new JTextField(20);
		this.topic.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				hm.changeTopic(topic.getText());
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub
				//hm.changeTopic(topic.getText());
			}
			
		});
		
		this.status = new JTextField(20);
		this.status.setEditable(false);
		this.message = new TextField(20);
		this.message.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String text = message.getText();
            	hm.addNewLine("["+userName+"]: "+text);
            	message.setText("");
			}
			
		});
		this.awareMessage = new JTextField(20);
		this.awareMessage.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String text = awareMessage.getText();
            	hm.addNewLine("["+userName+"]: "+text);
            	awareMessage.setText("");
			}
			
		});
		AwareDocumentListener adl = new AwareDocumentListener();
		adl.start();
		this.awareMessage.getDocument().addDocumentListener(adl);
		
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

		//this.add(Box.createRigidArea(new Dimension(0, 10)));
		this.add(comp);
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		HistoryModel model = (HistoryModel)o;
		HistoryModel.ChangeType type = (HistoryModel.ChangeType)arg;
		switch(type) {
			case ADD_NEW_LINE: {
				history.append(model.getLastInput()+"\n");
				break;
			}
			case CHANGE_TOPIC: {
				if (!model.getTopicInput().equals(this.topic.getText())) {
					this.topic.setText(model.getTopicInput());
				}
				break;
			}
			case SET_STATUS: {
				status.setText(model.getStatus());
				break;
			}
		}
		
	}
	
	private class AwareDocumentListener extends Thread implements DocumentListener {
		private long lastModifiedTime = 0;
		@Override
		public void changedUpdate(DocumentEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public synchronized void insertUpdate(DocumentEvent arg0) {
			// TODO Auto-generated method stub
			hm.setStatus(userName + " is typing...");
			this.lastModifiedTime = new Date().getTime();
			notify();
		}

		@Override
		public void removeUpdate(DocumentEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		public synchronized void run() {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			while (true) {
				try {
					wait(3000);
					long currentTime = new Date().getTime();
					if (currentTime - this.lastModifiedTime > 3000) {
						hm.setStatus(userName + " has typed.");
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}