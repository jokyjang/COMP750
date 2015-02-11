package echo.monolithic;

import util.annotations.Tags;
import util.pipe.InputGenerator;
import util.tags.ApplicationTags;
import bus.uigen.pipe.AConsoleModelBasedDemoerAndTester;
import bus.uigen.pipe.ConsoleModelBasedLauncher;
import bus.uigen.pipe.TranscriptBasedTester;
@Tags(ApplicationTags.ECHOER)
public class AMonolithicEchoConsoleModelBasedDemoerAndTester extends AConsoleModelBasedDemoerAndTester {
	public AMonolithicEchoConsoleModelBasedDemoerAndTester(boolean anInteractive) {
		super(anInteractive);
		

	}
	protected InputGenerator createDemoer() {
		return new  AMonolithicEchoInputGenerator();
	}
	
	protected TranscriptBasedTester createTranscriptBasedTester() {
		return new AMonolithicEchoTranscriptBasedTester();
	}
	
	protected ConsoleModelBasedLauncher createConsoleModelBasedLauncher() {
		return new AConsoleModelBasedLauncherOfMonolithicEchoer();
	}
}
