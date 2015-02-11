package echo.monolithic;

import bus.uigen.pipe.AConsoleModelBasedLauncher;
import bus.uigen.pipe.ConsoleModelBasedLauncher;

public class AConsoleModelBasedLauncherOfMonolithicEchoer extends AConsoleModelBasedLauncher implements ConsoleModelBasedLauncher{
	
	public Class[] mainClasses() {
		return new Class[] { echoClass() };
	}
	public Class echoClass() {
		return MonolithicEchoer.class;
		// return OTSessionManagerServerStarter.class;

	}

	
//	public  void launch() {	
////		Class[] classes = {
////				SessionManagerServerStarter.class,
////				AliceIM.class,
////				BobIM.class,
////				CathyIM.class				
////		};
//		MainClassLaunchingUtility.createInteractiveLauncher(composeMainClasses());
//	}
	
	public static void main(String args[]) {
		(new AConsoleModelBasedLauncherOfMonolithicEchoer()).launchWithConsoles();
	}

}
