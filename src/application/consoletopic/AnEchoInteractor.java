package application.consoletopic;
import static echo.monolithic.EchoUtilities.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import trace.echo.ListEditDisplayed;
import trace.echo.ListEditInput;
import trace.echo.modular.ListEditObserved;
import trace.echo.modular.OperationName;
import util.annotations.Tags;
import util.tags.ApplicationTags;
import util.tags.InteractiveTags;
import echo.modular.EchoerInteractor;
import echo.modular.SimpleList;
import echo.monolithic.EchoUtilities;

@Tags({ApplicationTags.ECHOER, InteractiveTags.INTERACTOR})
/**
 * This class is to change the model from SimpleList\<String\> to
 * SimpleList\<Character\>. Just want to see the differences and change
 * accordingly.
 * @author zhangzhx
 *
 */
public class AnEchoInteractor implements EchoerInteractor {	
	protected SimpleList<Character> topic;
	public AnEchoInteractor(SimpleList<Character> aTopic) {
		topic = aTopic;
	}
	public void doInput() {
		for (;;) {
			System.out.println(PROMPT);
			Scanner scanner = new Scanner(System.in);
			String nextInput = scanner.nextLine();
			if (nextInput.equals(QUIT)) {
				processQuit();
				break;
			} else if (nextInput.equals(TOPIC))
				printHistory();
			else if(nextInput.startsWith(ADD)) {
				String[] tokens = nextInput.split(" ");
				processAdd(Integer.parseInt(tokens[1]), tokens[2]);
			} else if(nextInput.startsWith(REMOVE)) {
				String[] tokens = nextInput.split(" ");
				processRemove(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
			} else
			    processInput(nextInput);
		}
	}
	private void processRemove(int from, int to) {
		// TODO Auto-generated method stub
		for(int i = to; i >= from; --i) {
			topic.observableRemove(i);
		}
	}
	private void processAdd(int idx, String anInput) {
		// TODO Auto-generated method stub
		for(int i = 0; i < anInput.length(); ++i) {
			Character c = anInput.charAt(i);
			ListEditInput.newCase(OperationName.ADD, idx + i, c, ApplicationTags.HISTORY, this);
			addToTopic(idx + i, c);
		}
	}
	protected void processInput(String anInput) {
		//String aFeedback = computeFeedback(anInput);
		for(int i = 0; i < anInput.length(); ++i) {
			Character c = anInput.charAt(i);
			ListEditInput.newCase(OperationName.ADD, topic.size(), c, ApplicationTags.HISTORY, this);
			addToTopic(c);
		}
	}
	protected void addToTopic(int idx, Character newValue) {
		topic.observableAdd(idx, newValue);
	}
	protected void addToTopic(Character newValue) {
		topic.observableAdd(newValue);
	}
	protected void displayOutput(String newValue) {
		System.out.println(newValue);
	}
	
	protected String computeFeedback(String anInput) {
		return EchoUtilities.echo(anInput);
	}
	
	protected void processQuit() {
		System.out.println("Quitting application");
	}
	public synchronized void printHistory() {
//		System.out.println(Thread.currentThread());

		System.out.println(toString());
//		for (int index = 0; index < history.size(); index++) {
//			System.out.print(history.get(index));
//		}
//		System.out.println();
	}
	
	@Override
	public void elementAdded(int anIndex, Object aNewValue) {
		ListEditObserved.newCase(OperationName.ADD, anIndex, aNewValue, ApplicationTags.HISTORY, this);
		displayOutput(toString());
		ListEditDisplayed.newCase(OperationName.ADD, anIndex, aNewValue, ApplicationTags.HISTORY, this);

	}
	public static List<Character> toList(SimpleList<Character> aTopic) {
		List<Character> retVal = new ArrayList();
		for (int index = 0; index < aTopic.size(); index++) {
			retVal.add(aTopic.get(index));			
//			stringBuilder.append((index == aHistory.size() - 1)? "\n":", ");
		}	
		return retVal;
	}
//	public static String toString(History aHistory) {
//		StringBuilder stringBuilder = new StringBuilder();
//		for (int index = 0; index < aHistory.size(); index++) {
//			stringBuilder.append(aHistory.get(index));			
//			stringBuilder.append((index == aHistory.size() - 1)? "\n":", ");
//		}		
//		return stringBuilder.toString();
//	}
	public String toString() {
		String aTopic = "";
		for(int i = 0; i < topic.size(); ++i) {
			aTopic += topic.get(i);
		}
		return aTopic;
	}
	@Override
	public void elementRemoved(int anIndex, Object aNewValue) {
		// TODO Auto-generated method stub
		ListEditObserved.newCase(OperationName.DELETE, anIndex, aNewValue, ApplicationTags.HISTORY, this);
		displayOutput(toString());
		ListEditDisplayed.newCase(OperationName.DELETE, anIndex, aNewValue, ApplicationTags.HISTORY, this);

	}

}
