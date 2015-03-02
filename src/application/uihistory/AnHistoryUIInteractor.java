package application.uihistory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import application.IMUI;
import application.IMUIListener;
import echo.general.HistoryEchoerInteractor;
import echo.general.SimpleList;

public class AnHistoryUIInteractor extends HistoryEchoerInteractor 
		implements IMUIListener {
	private SimpleList<String> history = null;
	private IMUI ui = null;
	public AnHistoryUIInteractor(SimpleList<String> h) {
		// TODO Auto-generated constructor stub
		history = h;
	}

	@Override
	public void addedToHistory(int anIndex, String aNewValue) {
		// TODO Auto-generated method stub
		ui.appendToHistory(aNewValue+"\n");
	}

	@Override
	public void processAddToHistory(String aNewValue) {
		// TODO Auto-generated method stub
		history.observableAdd(aNewValue);
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
