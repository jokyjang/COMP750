package application;

public class Tags {
	// Below are the model types
	String HISTORY = "History";
	String TOPIC = "Topic";
	String ANY_MODEL = "AnyModel";
	// Below are the view types
	String CONSOLE = "Console";
	String UI = "UI";
	String ANY_INTERACTOR = "AnyInteractor";
	// Below are the application types
	String ECHOER = "Echoer";	// single user
	String IM = "Instant Messaging";	// multiple users, just addition operation
	String EDITOR = "Editor";			// multiple users, addition and removal
	String TELEPOINTER = "Telepointer";	// multiple users, UI changing.
	
	String DEMOER = "Demoer"; 
	String TESTER = "Tester";
	// Below are the distribution types
	String REPLICATED_WINDOW = "ReplicatedWindow";
}
