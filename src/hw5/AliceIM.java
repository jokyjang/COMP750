package hw5;

import im.ExampleIMSession;
import trace.causal.CausalTracerSetter;
import trace.im.IMTracerSetter;
import trace.locking.LockTracerSetter;
import util.annotations.Tags;
import util.session.Communicator;
import util.tags.ApplicationTags;
import util.tags.DistributedTags;
import util.trace.Tracer;

@Tags({DistributedTags.CLIENT_1, DistributedTags.CAUSAL})
public class AliceIM implements ExampleIMSession{
	public static final String USER_NAME = DistributedTags.CLIENT_1;
	public static void main (String[] args) {
		Tracer.showInfo(true);
		CausalTracerSetter.traceCausal();
		//IMTracerSetter.traceIM();
		LockTracerSetter.setLockPrintStatus();
		new GuiComposerAndLauncher().composeAndLaunch(SESSION_SERVER_HOST, SESSION_NAME,
				USER_NAME, Communicator.DIRECT);
	}
	
}
