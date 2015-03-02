package application;

public class Tags {
	// Below are the model types
	public static String HISTORY = "History";
	public static String TOPIC = "Topic";
	public static String ANY_MODEL = "AnyModel";
	// Below are the view types
	public static String CONSOLE = "Console";
	public static String UI = "UI";
	public static String ANY_INTERACTOR = "AnyInteractor";
	// Below are the application types
	public static String ECHOER = "Echoer";	// single user
	public static String IM = "Instant Messaging";	// multiple users, just addition operation
	public static String EDITOR = "Editor";			// multiple users, addition and removal
	public static String TELEPOINTER = "Telepointer";	// multiple users, UI changing.
	
	public static String DEMOER = "Demoer"; 
	public static String TESTER = "Tester";
	// Below are the distribution types
	public static String REPLICATED_WINDOW = "ReplicatedWindow";
}
