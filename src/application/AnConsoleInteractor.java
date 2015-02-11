package application;
import static echo.monolithic.EchoUtilities.HISTORY;
import static echo.monolithic.EchoUtilities.QUIT;

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

import echo.modular.ListObserver;
import echo.modular.SimpleList;
import echo.monolithic.EchoUtilities;

@Tags({ApplicationTags.ECHOER, InteractiveTags.INTERACTOR})
public class AnConsoleInteractor implements ListObserver<String>, EchoerInteractor {	
	protected SimpleList<String> history;
	public AnConsoleInteractor(SimpleList<String> aHistory) {
		history = aHistory;
	}
	public void doInput() {
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
	protected void processInput(String anInput) {
		String aFeedback = computeFeedback(anInput);
		ListEditInput.newCase(OperationName.ADD, history.size(), aFeedback, ApplicationTags.HISTORY, this);
		addToHistory(aFeedback);
	}
	protected void addToHistory(String newValue) {
		history.observableAdd(newValue);
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

		System.out.println(AnConsoleInteractor.toString(history));
//		for (int index = 0; index < history.size(); index++) {
//			System.out.print(history.get(index));
//		}
//		System.out.println();
	}
	
	@Override
	public void elementAdded(int anIndex, String aNewValue) {
		ListEditObserved.newCase(OperationName.ADD, anIndex, aNewValue, ApplicationTags.HISTORY, this);
		displayOutput(history.get(anIndex));
		ListEditDisplayed.newCase(OperationName.ADD, anIndex, aNewValue, ApplicationTags.HISTORY, this);

	}
	public static List<String> toList(SimpleList<String> aHistory) {
		List<String> retVal = new ArrayList();
		for (int index = 0; index < aHistory.size(); index++) {
			retVal.add(aHistory.get(index));			
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
	public static String toString(SimpleList aHistory) {
		return EchoUtilities.toString(toList(aHistory));
	}
	@Override
	public void elementRemoved(int anIndex, String aNewValue) {
		// TODO Auto-generated method stub
		
	}

}
