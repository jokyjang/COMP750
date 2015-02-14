package application;

import java.awt.event.ActionListener;
import javax.swing.event.DocumentListener;

public interface IMUIListener {

	DocumentListener getTopicDocumentListener();

	ActionListener getMessageActionListener();

	ActionListener getAwareMessageActionListener();

	DocumentListener getAwareMessageDocumentListener();
	
	/*AwareDocumentListener adl = new AwareDocumentListener();
	adl.start();
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
	*/
	
	/*new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			String text = awareMessage.getText();
        	hm.addNewLine("["+userName+"]: "+text);
        	awareMessage.setText("");
		}
		
	}*/
	
	/*new DocumentListener() {

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
		
	}*/
	
}
