package echo.monolithic;

import bus.uigen.pipe.ConsoleModelBasedDemoerAndTester;
import bus.uigen.pipe.DemoerAndTester;

public class LauncherOfMonolithicEchoDemoer {
	public static DemoerAndTester createOldDemoer(boolean anInteractive) {
		return new AMonolithicEchoDemoerAndTester(anInteractive);
	}
	public static ConsoleModelBasedDemoerAndTester createNewDemoer(boolean anInteractive) {
		return new AMonolithicEchoConsoleModelBasedDemoerAndTester(anInteractive);
	}
	public static void oldDemoerAndTester() {
		DemoerAndTester aDemoer = createOldDemoer(true);
		aDemoer.demo();
		aDemoer.terminate();
	}
	public static void newDemoerAndTester() {
		ConsoleModelBasedDemoerAndTester aDemoer = createNewDemoer(true);
		aDemoer.demo();
		aDemoer.terminate();
	}
	public static void main(String args[]) {
//		oldDemoerAndTester();
		newDemoerAndTester();

	}

}
