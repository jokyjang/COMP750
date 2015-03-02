package application.consoletopic;

import static echo.monolithic.EchoUtilities.ADD;
import static echo.monolithic.EchoUtilities.PROMPT;
import static echo.monolithic.EchoUtilities.QUIT;
import static echo.monolithic.EchoUtilities.REMOVE;
import static echo.monolithic.EchoUtilities.TOPIC;

import java.util.Scanner;

import echo.general.SimpleList;
import echo.general.TopicEchoerInteractor;

public class AnTopicEchoerInteractor extends TopicEchoerInteractor{
	protected SimpleList<Character> topic;
	public AnTopicEchoerInteractor(SimpleList<Character> aTopic) {
		topic = aTopic;
	}
	
	@Override
	public void start() {
		for (;;) {
			System.out.println(PROMPT);
			Scanner scanner = new Scanner(System.in);
			String nextInput = scanner.nextLine();
			if (nextInput.equals(QUIT)) {
				processQuit();
				break;
			} else if (nextInput.equals(TOPIC))
				printTopic();
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
	
	private void processInput(String nextInput) {
		// TODO Auto-generated method stub
		System.out.println("Error format!");
	}

	private void processRemove(int parseInt, int parseInt2) {
		// TODO Auto-generated method stub
		processRemoveFromTopic(parseInt, parseInt2);
	}

	private void processAdd(int parseInt, String string) {
		// TODO Auto-generated method stub
		for(int i = 0; i < string.length(); ++i) {
			Character c = string.charAt(i);
			this.processAddToTopic(parseInt + i, c);
		}
	}

	private void printTopic() {
		// TODO Auto-generated method stub
		System.out.println(getTopic());
	}

	private String getTopic() {
		// TODO Auto-generated method stub
		String topicString = "";
		for(int i = 0; i < topic.size(); ++i) {
			topicString += topic.get(i);
		}
		return topicString;
	}

	private void processQuit() {
		// TODO Auto-generated method stub
		System.out.println("quit");
	}

	@Override
	public void addedToTopic(int anIndex, Character aNewValue) {
		// TODO Auto-generated method stub
		printTopic();
	}

	@Override
	public void removedFromTopic(int anIndex, Character aNewValue) {
		// TODO Auto-generated method stub
		printTopic();
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

}
