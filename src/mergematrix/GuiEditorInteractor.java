package mergematrix;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import trace.echo.ListEditDisplayed;
import trace.echo.ListEditInput;
import trace.echo.modular.ListEditObserved;
import trace.echo.modular.OperationName;

public class GuiEditorInteractor implements ListObserver, DocumentListener {

	private GUIView viewer = null;
	private boolean displaySelf = true;
	private boolean listenChange = true;
	protected ReplicatedSimpleList<Character> topic;

	public GuiEditorInteractor(ReplicatedSimpleList<Character> aTopic) {
		topic = aTopic;
	}

	public void setGUI(GUIView imView) {
		// TODO Auto-generated method stub
		this.viewer = imView;
	}

	protected void addToTopic(int index, Character newValue) {
		topic.replicatedAdd(index, newValue);
	}
	protected void removeFromTopic(int index) {
		topic.replicatedRemove(index);
	}
	protected void updateTopic(int index, Character newValue) {
		topic.replicatedReplace(index, newValue);
	}

	protected void processQuit() {
		System.out.println("Quitting application");
	}

	protected void processInsert(int index, Character anInput) {
		//String aFeedback = computeFeedback(anInput);
		ListEditInput.newCase(OperationName.ADD, index, anInput, ApplicationTags.EDITOR, this);
		addToTopic(index, anInput);
	}
	protected void processRemove(int index) {
		ListEditInput.newCase(OperationName.DELETE, index, topic.get(index), ApplicationTags.EDITOR, this);
		removeFromTopic(index);
	}
	
	protected void processReplace(int index, Character anInput) {
		ListEditInput.newCase(OperationName.REPLACE, index, anInput, ApplicationTags.EDITOR, this);
		updateTopic(index, anInput);
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
		if(viewer == null) {
			System.out.println("[Topic]: " + sb.toString());
		} else {
			this.viewer.setTopic(sb.toString());
		}
		listenChange = true;
	}

	@Override
	public void elementAdded(int anIndex, Object aNewValue) {
		ListEditObserved.newCase(OperationName.ADD, anIndex, aNewValue,
				ApplicationTags.EDITOR, this);
		displayOutput();
		ListEditDisplayed.newCase(OperationName.ADD, anIndex, aNewValue,
				ApplicationTags.EDITOR, this);
	}

	@Override
	public void elementRemoved(int anIndex, Object aNewValue) {
		// TODO Auto-generated method stub
		ListEditObserved.newCase(OperationName.DELETE, anIndex, aNewValue,
				ApplicationTags.EDITOR, this);
		displayOutput();
		ListEditDisplayed.newCase(OperationName.DELETE, anIndex, aNewValue,
				ApplicationTags.EDITOR, this);
	}

	@Override
	public void elementReplaced(int anIndex, Object aNewValue) {
		// TODO Auto-generated method stub
		ListEditObserved.newCase(OperationName.REPLACE, anIndex, aNewValue,
				ApplicationTags.EDITOR, this);
		displayOutput();
		ListEditDisplayed.newCase(OperationName.REPLACE, anIndex, aNewValue,
				ApplicationTags.EDITOR, this);
	}

	@Override
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

	@Override
	public void removeUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		if(!listenChange) return;
		displaySelf = false;
		int offset = e.getOffset();
		for (int i = 0; i < e.getLength(); ++i) {
			this.processRemove(offset);
		}
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		if(!listenChange) return;
		displaySelf = false;
		int offset =  e.getOffset();
		int length = e.getLength();
		String document = "NULL";
		try {
			document = e.getDocument().getText(offset, length);
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Event: " + offset + ", " + length + ", " + document);
	}
}
