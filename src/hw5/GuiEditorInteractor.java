package hw5;

import im.IMUtililties;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import trace.echo.ListEditDisplayed;
import trace.echo.ListEditInput;
import trace.echo.modular.ListEditObserved;
import trace.echo.modular.OperationName;
import util.session.Communicator;
import util.tags.ApplicationTags;
import echo.modular.ListObserver;

public class GuiEditorInteractor implements ListObserver, DocumentListener {

	private GUIView viewer = null;
	private boolean displaySelf = true;
	private boolean listenChange = true;
	protected ReplicatedSimpleList<Character> topic;
	protected Communicator communicator;

	public GuiEditorInteractor(ReplicatedSimpleList<Character> aTopic,
			Communicator aCommunicator) {
		topic = aTopic;
		communicator = aCommunicator;
	}

	public void setGUI(GUIView imView) {
		// TODO Auto-generated method stub
		this.viewer = imView;
	}

	protected void addToTopic(int index, Character newValue) {
		// ((ReplicatedHistory) history).replicatedAdd(history.size(), newValue);
		topic.replicatedAdd(index, newValue);
		//topic.add(index, newValue);
	}
	protected void removeFromTopic(int index) {
		topic.replicatedRemove(index);
	}

	protected void processQuit() {
		System.out.println("Quitting application");
		communicator.leave();
	}

	protected String computeFeedback(String anInput) {
		return IMUtililties.remoteEcho(anInput, communicator.getClientName());
	}

	// protected void displayOutput(String newValue) {
	// System.out.println(EchoUtilities.echo(newValue));
	// }

	protected void processInsert(int index, Character anInput) {
		//String aFeedback = computeFeedback(anInput);
		ListEditInput.newCase(OperationName.ADD, index, anInput, ApplicationTags.EDITOR, this);
		addToTopic(index, anInput);
	}
	protected void processRemove(int index) {
		ListEditInput.newCase(OperationName.DELETE, index, topic.get(index), ApplicationTags.EDITOR, this);
		removeFromTopic(index);
	}

	protected void displayOutput() {
		if(!displaySelf) {
			displaySelf = true;
			return;
		}
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < this.topic.size(); i++) {
			sb.append(this.topic.get(i));
		}
		listenChange = false;
		this.viewer.setTopic(sb.toString());
		listenChange = true;
		//this.viewer.appendHistory( sb.toString() + "\n");
		//System.out.println(sb.toString());
		//if (this.viewer.getTopic().equalsIgnoreCase(newValue))
		//	return;
		//this.viewer.setTopic(newValue);
	}

	
	public void elementAdded(int anIndex, Object aNewValue) {
		ListEditObserved.newCase(OperationName.ADD, anIndex, aNewValue,
				ApplicationTags.EDITOR, this);
		displayOutput();
		ListEditDisplayed.newCase(OperationName.ADD, anIndex, aNewValue,
				ApplicationTags.EDITOR, this);
	}

	
	public void elementRemoved(int anIndex, Object aNewValue) {
		// TODO Auto-generated method stub
		ListEditObserved.newCase(OperationName.DELETE, anIndex, aNewValue,
				ApplicationTags.EDITOR, this);
		displayOutput();
		ListEditDisplayed.newCase(OperationName.DELETE, anIndex, aNewValue,
				ApplicationTags.EDITOR, this);
	}

	
	public void insertUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("listener listenerChange: "+listenChange+" displaySelf: "+displaySelf);
		if(!listenChange) return;
		displaySelf = false;
		Document doc = e.getDocument();
		try {
			int offset = e.getOffset();
			String text = doc.getText(0, doc.getLength());
			for(int i = 0; i < e.getLength(); ++i) {
				this.processInsert(offset+i, text.charAt(offset+i));
			}
			//System.out.println(text.substring(offset, offset+e.getLength()));
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	
	public void removeUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		if(!listenChange) return;
		displaySelf = false;
		int offset = e.getOffset();
		for (int i = 0; i < e.getLength(); ++i) {
			this.processRemove(offset);
		}
	}

	
	public void changedUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub

	}
	
	public void doInput() {
		
	}
}
