package echo.monolithic;

import util.trace.console.ConsoleTraceSetter;
import bus.uigen.pipe.ConsoleModelBasedDemoerAndTester;

public class LauncherOfMonolithicEchoCorrectLogGenerator extends LauncherOfMonolithicEchoDemoer{
	public static ConsoleModelBasedDemoerAndTester createDemoer() {
		return new AMonolithicEchoConsoleModelBasedDemoerAndTester(false);
	}
	public static void main(String args[]) {
		ConsoleTraceSetter.traceConsole(); // needed for comparing outputs
		ConsoleModelBasedDemoerAndTester aDemoer = createDemoer();
		aDemoer.getTranscriptBasedTester().setCorrectSubFolder("AMonolithicEchoDemoerAndTester");
		Boolean retVal = aDemoer.executeLoadAndTest(true, false);


		System.out.println("Test result:" + retVal);


		aDemoer.terminate();
	}

}
