package echo.general;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import application.IMUI;
import application.IMUIListener;

public class AnUIInteractor implements EchoerInteractor, IMUIListener{
	private SimpleList<Character> topic = null;
	private SimpleList<String> history = null;
	private IMUI ui = null;
	public AnUIInteractor(SimpleList<Character> t, SimpleList<String> h) {
		// TODO Auto-generated constructor stub
		topic = t;
		history = h;
	}

	@Override
	public void addedToHistory(int anIndex, String aNewValue) {
		// TODO Auto-generated method stub
		ui.appendToHistory(aNewValue+"\n");
	}

	@Override
	public void addedToTopic(int anIndex, Character aNewValue) {
		// TODO Auto-generated method stub
		ui.insertToTopic(anIndex, aNewValue);
	}

	@Override
	public void removedFromTopic(int anIndex, Character aNewValue) {
		// TODO Auto-generated method stub
		ui.removeFromTopic(anIndex, anIndex+1);
	}

	@Override
	public void processAddToHistory(String aNewValue) {
		// TODO Auto-generated method stub
		history.observableAdd(aNewValue);
	}

	@Override
	public void processAddToTopic(int anIndex, Character aNewValue) {
		// TODO Auto-generated method stub
		topic.observableAdd(anIndex, aNewValue);
	}

	@Override
	public void processRemoveFromTopic(int from, int to) {
		// TODO Auto-generated method stub
		for(int i = to; i >= from; --i) {
			topic.observableRemove(i);
		}
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		ui = new IMUI();
		ui.setListener(this);
	}

	@Override
	public DocumentListener getTopicDocumentListener() {
		// TODO Auto-generated method stub
		return new TopicDocumentListener();
	}

	@Override
	public ActionListener getMessageActionListener() {
		// TODO Auto-generated method stub
		return new MessageActionListener();
	}

	@Override
	public ActionListener getAwareMessageActionListener() {
		// TODO Auto-generated method stub
		return new AwareMessageActionListener();
	}

	@Override
	public DocumentListener getAwareMessageDocumentListener() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private class TopicDocumentListener implements DocumentListener {

		@Override
		public void insertUpdate(DocumentEvent e) {
			// TODO Auto-generated method stub
			/*
			try {
				int offset = e.getOffset();
				int length = e.getLength();
				String text = e.getDocument().getText(offset, length);
				for(int i = offset; i < offset+length; ++i) {
					processAddToTopic(i, text.charAt(i-offset));
				}
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class MessageActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String text = ui.getMessageText();
			processAddToHistory(text);
			ui.setMessageText("");
		}
	}

	private class AwareMessageActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String text = ui.getAwareMessageText();
			processAddToHistory(text);
			ui.setAwareMessageText("");
		}

	}
}