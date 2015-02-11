package echo.monolithic;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static echo.monolithic.EchoUtilities.*;
import trace.echo.ListEditDisplayed;
import trace.echo.ListEditInput;
import trace.echo.ListEditMade;
import trace.echo.modular.ListEditObserved;
import trace.echo.modular.OperationName;
import util.annotations.Tags;
import util.tags.ApplicationTags;
import util.tags.InteractiveTags;
import util.trace.ImplicitKeywordKind;
import util.trace.Tracer;
@Tags({ApplicationTags.ECHOER})
/**
 * This class is the most basic format of chatting.
 * 
 * This class is showing monolithic echoer which integrates data and UI in the same class for 
 * a single user.
 * 
 * @author zhangzhx
 *
 */
public class MonolithicEchoer  {	
	protected static List<String> history = new ArrayList();	//data
	public static void main(String[] anArgs) {
		Tracer.showInfo(false);
		Tracer.setImplicitPrintKeywordKind(ImplicitKeywordKind.OBJECT_CLASS_NAME);
		Tracer.setKeywordPrintStatus(MonolithicEchoer.class, true);

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
			    processInput(nextInput);
		}
	}
	/**
	 * Output `newValue' to the console.
	 * @param newValue the value to be outputed.
	 */
	protected static void displayOutput(String newValue) {
		System.out.println(newValue);
	}
	/**
	 * Append `newValue' to history.
	 * @param newValue new string to be appended to history.
	 */
	protected  static void addToHistory(String newValue) {
		history.add(history.size(), newValue);
	}
	/**
	 * Process a new input in the chat session.
	 * @param anInput an input from user.
	 */
	protected static void processInput(String anInput) {
//		System.out.println("received input");
		// received input
		ListEditInput.newCase(OperationName.ADD, history.size(), anInput, ApplicationTags.HISTORY, MonolithicEchoer.class);		
		String aFeedback = EchoUtilities.echo(anInput);
		addToHistory(aFeedback);
		// added input to history
		ListEditMade.newCase(OperationName.ADD, history.size(), anInput, ApplicationTags.HISTORY, MonolithicEchoer.class);		
		displayOutput(aFeedback);
		// displayed the input
		ListEditDisplayed.newCase(OperationName.ADD, history.size(), anInput, ApplicationTags.HISTORY, MonolithicEchoer.class);
	}
	protected static void processQuit() {
		System.out.println("Quitting application");
	}
	/**
	 * Translate history content into the format of String.
	 * @param aHistory history object.
	 * @return a string format of history.
	 */
	public static String toString(List aHistory) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int index = 0; index < aHistory.size(); index++) {
			stringBuilder.append(aHistory.get(index));			
			stringBuilder.append((index == aHistory.size() - 1)? "\n":", ");
		}		
		return stringBuilder.toString();
	}
	public static  void printHistory() {
		System.out.println(toString(history));
	}
	

}
