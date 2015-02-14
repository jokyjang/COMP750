package echo.general;

import static echo.monolithic.EchoUtilities.ADD;
import static echo.monolithic.EchoUtilities.REMOVE;
import static echo.monolithic.EchoUtilities.TOPIC;

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

@Tags({ApplicationTags.ECHOER, InteractiveTags.INTERACTOR})
public class AnConsoleInteractor implements EchoerInteractor {
	public static final String QUIT = "quit";
	public static final String HISTORY = "history";
	public static final String ECHO_INDICATOR = "[Echo]";
	public static final String TOPIC = "topic";
	public static final String ADD = "add";
	public static final String REMOVE = "remove";
	public static final String PROMPT = "Please enter an input line or " +
			 QUIT + ", " + HISTORY + ", " + TOPIC + ", " + ADD + ", " + REMOVE;
	
	protected SimpleList<Character> topic;
	protected SimpleList<String> history;
	
	public AnConsoleInteractor(SimpleList<Character> aTopic, SimpleList<String> aHistory) {
		topic = aTopic;
		history = aHistory;
	}
	
	public void start() {
		for (;;) {
			System.out.println(PROMPT);
			Scanner scanner = new Scanner(System.in);
			String nextInput = scanner.nextLine();
			if (nextInput.equals(QUIT)) {
				processQuit();
				break;
			} else if (nextInput.equals(HISTORY))
				printHistory();
			else if (nextInput.equals(TOPIC))
				printTopic();
			else if(nextInput.startsWith(ADD)) {
				String[] tokens = nextInput.split(" ");
				processAddToTopic(Integer.parseInt(tokens[1]), tokens[2]);
			} else if(nextInput.startsWith(REMOVE)) {
				String[] tokens = nextInput.split(" ");
				processRemoveFromTopic(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
			} else
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

	private void printTopic() {
		// TODO Auto-generated method stub
		System.out.println(getTopic());
	}
	
	private String getTopic() {
		String t = "";
		for(int i = 0; i < topic.size(); ++i) {
			t += topic.get(i);
		}
		return t;
	}
	
	private void processAddToTopic(int idx, String anInput) {
		// TODO Auto-generated method stub
		for(int i = 0; i < anInput.length(); ++i) {
			Character c = anInput.charAt(i);
			processAddToTopic(idx+i, c);
		}
	}
	
	protected void displayOutput(String newValue) {
		System.out.println(newValue);
	}
	
	protected void processQuit() {
		System.out.println("Quitting application");
	}

	@Override
	public void processAddToTopic(int anIndex, Character aNewValue) {
		// TODO Auto-generated method stub
		ListEditInput.newCase(OperationName.ADD, anIndex, aNewValue, ApplicationTags.HISTORY, this);
		topic.observableAdd(anIndex, aNewValue);
	}

	@Override
	public void processRemoveFromTopic(int from, int to) {
		// TODO Auto-generated method stub
		for (int i = to; i >= from; --i) {
			topic.observableRemove(i);
		}
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
	public void addedToTopic(int anIndex, Character aNewValue) {
		// TODO Auto-generated method stub
		displayOutput(getTopic());
	}
	@Override
	public void removedFromTopic(int from, Character to) {
		displayOutput(getTopic());
	}

}
