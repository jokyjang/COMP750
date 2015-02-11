package echo.monolithic;

import java.util.List;

import trace.echo.EchoTraceChecker;
import util.annotations.Tags;
import util.misc.ThreadSupport;
import util.pipe.ConsoleModel;
import util.tags.ApplicationTags;
import util.trace.Traceable;
import bus.uigen.pipe.ADemoerAndTester;
import bus.uigen.pipe.DemoerAndTester;
@Tags(ApplicationTags.ECHOER)
public class AMonolithicEchoDemoerAndTester extends ADemoerAndTester implements DemoerAndTester{
	protected ConsoleModel echoConsole;
	protected boolean echoTestSucceeded;
	protected boolean echoTranscriptTestSucceeded;

	public static long DEFAULT_TIME_OUT = 5000;
//	
//	protected MainClassListLauncher launcher;
	protected String[] poem = {
			"The woods are lovely dark and deep",			 
			"But I have promises to keep",  			 
			"And miles to go before I sleep"
	};
	
	public AMonolithicEchoDemoerAndTester() {
//		computeFinalHistory();
	}
	public AMonolithicEchoDemoerAndTester(boolean anInteractive) {
		super(anInteractive);
	}
	
	
	
	
	@Override
	public void executeAll() {		
		consoleModels = launcher.getOrCreateConsoleModels();
		echoConsole = consoleModels.get(0);
		launcher.addConsolesPropertyChangeListener(this);
		launcher.executeAll();	
		consoleModelsInitialized();
		echoConsole.setInput(poem[0]);
		// replace these with wait for clearance etc
//		ThreadSupport.sleep(1000);
		echoConsole.setInput(poem[1]);
		ThreadSupport.sleep(1000);
		echoConsole.setInput(poem[2]);
//		ThreadSupport.sleep(1000);
		ThreadSupport.sleep(getTimeOut());
		notifyInteractionTermination();

	
	}
	public static long getTimeOut() {
		return DEFAULT_TIME_OUT;
	}
	
	
	protected Class echoClass() {
		return 	MonolithicEchoer.class;
	}
	
	protected Class[] composeMainClasses() {
		return new Class[] {
			echoClass()			
		};
	}
	
//	public Boolean executeLoadAndTest(Boolean aGenerateCorrectTranscripts, Boolean aTestAgainstCorrectTranscripts) {
//		super.executeLoadAndTest(aGenerateCorrectTranscripts, aTestAgainstCorrectTranscripts);
////		return EchoTraceChecker.checkEchoer(getGlobalTraceableList());
//		boolean retVal = EchoTraceChecker.checkInputOutSequences(getGlobalTraceableList());
//		return retVal;
//	}
	
	public boolean intraSequenceTest(List<Traceable> aTraceableList) {
//		System.out.println("Intra Sequence Check: Echoer");
		echoTestSucceeded =  EchoTraceChecker.matchInputOutSequences(aTraceableList);
		System.out.println("Intra Sequence Check: Echoer:" + echoTestSucceeded);
		return echoTestSucceeded;
	}
	@Override
	public Boolean test() {
//		echoTestSucceeded = EchoTraceChecker.checkInputOutSequences(getGlobalTraceableList());
//		return echoTestSucceeded;
		return intraSequenceTest(getGlobalTraceableList());
	}
	protected   Boolean testAgainstCorrectTranscripts(List<Traceable> aTestTraceableList, 
			List<Traceable> aCorrectTraceableList) {
		echoTranscriptTestSucceeded = EchoTraceChecker.compareInputOutSequences(aTestTraceableList, aCorrectTraceableList);
		System.out.println("Inter sequence echo:" + echoTranscriptTestSucceeded);
		return echoTranscriptTestSucceeded;
				
	}
	@Override
	public Boolean testAgainstCorrectTranscripts() {
		return testAgainstCorrectTranscripts(getGlobalTraceableList(), getCorrectGlobalTraceableList());
//		System.out.println("Comparing echo sequences");
//		echoTranscriptTestSucceeded = EchoTraceChecker.compareInputOutSequences(getGlobalTraceableList(), getCorrectGlobalTraceableList());
//		System.out.println("Inter sequence echo:" + echoTranscriptTestSucceeded);
//
//		return echoTranscriptTestSucceeded;
	}
	
//	public Boolean test(Boolean aTestAgainstCorrectTranscripts) {
////		return EchoTraceChecker.checkEchoer(getGlobalTraceableList());
//		boolean retVal1 = EchoTraceChecker.checkInputOutSequences(getGlobalTraceableList());
//		if (aTestAgainstCorrectTranscripts)
//			return retVal1 & EchoTraceChecker.compareInputOutSequences(getGlobalTraceableList(), getCorrectGlobalTraceableList());
//		else
//			return retVal1;
//	}




	


}
