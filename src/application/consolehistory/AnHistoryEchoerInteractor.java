package application.consolehistory;

import java.util.Scanner;

import trace.echo.ListEditDisplayed;
import trace.echo.ListEditInput;
import trace.echo.modular.ListEditObserved;
import trace.echo.modular.OperationName;
import util.tags.ApplicationTags;
import echo.general.HistoryEchoerInteractor;
import echo.general.SimpleList;

public class AnHistoryEchoerInteractor extends HistoryEchoerInteractor{
	public static final String QUIT = "quit";
	public static final String HISTORY = "history";
	public static final String ECHO_INDICATOR = "[Echo]";
	public static final String PROMPT = "Please enter an input line or " +
			 QUIT + ", " + HISTORY;
	
	protected SimpleList<String> history;

	public AnHistoryEchoerInteractor(SimpleList<String> aHistory) {
		history = aHistory;
	}

	@Override
	public void processAddToHistory(String aNewValue) {
		// TODO Auto-generated method stub
		ListEditInput.newCase(OperationName.ADD, history.size(), aNewValue, ApplicationTags.HISTORY, this);
		history.observableAdd(aNewValue);
	}
	
	@Override
	public void addedToHistory(int anIndex, String aNewValue) {
		// TODO Auto-generated method stub
		ListEditObserved.newCase(OperationName.ADD, anIndex, aNewValue, ApplicationTags.HISTORY, this);
		displayOutput(history.get(anIndex));
		ListEditDisplayed.newCase(OperationName.ADD, anIndex, aNewValue, ApplicationTags.HISTORY, this);
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		for (;;) {
			System.out.println(PROMPT);
			Scanner scanner = new Scanner(System.in);
			String nextInput = scanner.nextLine();
			if (nextInput.equals(QUIT)) {
				processQuit();
				break;
			} else if (nextInput.equals(HISTORY))
				printHistory();
			else
				processAddToHistory(nextInput);
		}
	}
	
	public synchronized void printHistory() {
		// TODO Auto-generated method stub
		System.out.println(getHistory());
	}
	
	private String getHistory() {
		// TODO Auto-generated method stub
		String t = "";
		for(int i = 0; i < history.size(); ++i) {
			t += history.get(i);
			if(i < history.size() - 1) t += ", ";
		}
		return t;
	}
	
	protected void displayOutput(String newValue) {
		System.out.println(newValue);
	}
	
	protected void processQuit() {
		System.out.println("Quitting application");
	}
}
