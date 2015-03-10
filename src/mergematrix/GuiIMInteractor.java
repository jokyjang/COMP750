package mergematrix;

import static echo.monolithic.EchoUtilities.HISTORY;
import static echo.monolithic.EchoUtilities.QUIT;
import im.IMUtililties;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Scanner;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import trace.echo.ListEditDisplayed;
import trace.echo.ListEditInput;
import trace.echo.modular.ListEditObserved;
import trace.echo.modular.OperationName;
import util.session.Communicator;
import util.tags.ApplicationTags;
import echo.modular.ListObserver;
import echo.monolithic.EchoUtilities;

public class GuiIMInteractor extends Thread
		implements ListObserver, DocumentListener, ActionListener {
	private GUIView viewer = null;
	protected ReplicatedSimpleList<String> history;
	private String userName = null;
	
	public GuiIMInteractor(ReplicatedSimpleList<String> aHistory) {
		history = aHistory;
		userName = aHistory.getClientName();
	}	
	
	public void setGUI(GUIView imView) {
		// TODO Auto-generated method stub
		this.viewer = imView;
	}
	
	protected void addToHistory(String newValue) {
//		((ReplicatedHistory) history).replicatedAdd(history.size(), newValue);
		history.replicatedAdd(newValue);

	}
	protected void processQuit() {
		System.out.println("Quitting application");
	}
	
	protected String computeFeedback(String anInput) {
		return IMUtililties.remoteEcho(anInput, userName);
	}
//	protected void displayOutput(String newValue) {
//		System.out.println(EchoUtilities.echo(newValue));
//	}
	
	protected void processInput(String anInput) {
		String aFeedback = computeFeedback(anInput);
		ListEditInput.newCase(OperationName.ADD, history.size(), aFeedback, ApplicationTags.IM, this);
		addToHistory(aFeedback);
	}
	
	protected void displayOutput(String newValue) {
		//this.viewer.appendHistory(newValue+"\n");
		StringBuilder allHistory = new StringBuilder();
		for(int i = 0; i < history.size(); ++i) {
			allHistory.append(history.get(i)+"\n");
		}
		this.viewer.resetHistory(allHistory.toString());
		System.out.println(allHistory.toString());
	}
	
	@Override
	public void elementAdded(int anIndex, Object aNewValue) {
		ListEditObserved.newCase(OperationName.ADD, anIndex, aNewValue, ApplicationTags.IM, this);
		displayOutput(history.get(anIndex));
		ListEditDisplayed.newCase(OperationName.ADD, anIndex, aNewValue, ApplicationTags.IM, this);
	}
	
	@Override
	public void elementRemoved(int anIndex, Object aNewValue) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (!(e.getSource() instanceof JTextField)) {
			return;
		}
		JTextField tf = (JTextField) e.getSource();
		if (tf == this.viewer.getMessagewidget()) {
			String text = tf.getText();
			this.processInput(text);
			tf.setText("");
		} else if (tf == this.viewer.getAwareMessageWidget()) {
			String text = tf.getText();
			this.processInput(text);
			tf.setText("");
			this.viewer.setStatus("");
		}
	}
		
	private long lastModifiedTime = 0;

	@Override
	public synchronized void insertUpdate(DocumentEvent arg0) {
		// TODO Auto-generated method stub
		this.lastModifiedTime = new Date().getTime();
		this.viewer.setStatus(this.userName + " is typing!");
		notify();
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public synchronized void run() {
		try {
			wait();
		} catch (InterruptedException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (true) {
			try {
				wait(3000);
				long currentTime = new Date().getTime();
				if (currentTime - this.lastModifiedTime > 3000) {
					if(!this.viewer.getStatus().equals("")) {
						this.viewer.setStatus(this.userName + " has typed!");
					}
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void doInput() {
		// TODO Auto-generated method stub
		
		this.start();
		
		for (;;) {
			System.out.println(EchoUtilities.PROMPT);
			Scanner scanner = new Scanner(System.in);
			String nextInput = scanner.nextLine();
			if (nextInput.equals(QUIT)) {
				processQuit();
				break;
			} else if (nextInput.equals(HISTORY))
				printHistory();
			else
			    processInput(nextInput);
		}
	}

	private void printHistory() {
		// TODO Auto-generated method stub
		StringBuilder stringBuilder = new StringBuilder();
		for (int index = 0; index < this.history.size(); index++) {
			stringBuilder.append(this.history.get(index));			
			stringBuilder.append((index == this.history.size() - 1)? "\n":", ");
		}		
		System.out.println(stringBuilder.toString());
	}
}
