package im;

import util.tags.DistributedTags;
import bus.uigen.pipe.AConsoleModelBasedLauncher;

public class ALauncherOfIM extends AConsoleModelBasedLauncher implements LauncherOfIM {
	
	public Class[] mainClasses() {
		return new Class[] { 
				sessionManagerClass(), 		
				aliceClass(), 
				bobClass(),		
				cathyClass() 
			};
	}
	public String[] processNames() {
		return new String[] {
				sessionManagerName(), 		
				aliceName(), 
				bobName(),		
				cathyName() 
		};
	}
	public Class sessionManagerClass() {
		return SessionManagerServerStarter.class;
		// return OTSessionManagerServerStarter.class;

	}
	public String sessionManagerName() {
		return DistributedTags.SESSION_MANAGER;
	}

	public Class aliceClass() {
		return AliceIM.class;
		// return AliceOTIM.class;
	}
	public String aliceName() {
		return AliceIM.USER_NAME;
		// return AliceOTIM.class;
	}

	public Class bobClass() {
		return BobIM.class;
		// return BobOTIM.class;
	}
	public String bobName() {
		return BobIM.USER_NAME;
		// return AliceOTIM.class;
	}

	public Class cathyClass() {
		return CathyIM.class;
		// return CathyOTIM.class;
	}
	
	public String cathyName() {
		return CathyIM.USER_NAME;
		// return AliceOTIM.class;
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
		(new ALauncherOfIM()).launchWithConsoles();
	}

}
