package hw5;

import im.ExampleIMSession;
import trace.causal.CausalTracerSetter;
import trace.im.IMTracerSetter;
import util.annotations.Tags;
import util.session.Communicator;
import util.tags.ApplicationTags;
import util.tags.DistributedTags;
import util.trace.Tracer;

@Tags({DistributedTags.CLIENT_4, DistributedTags.CAUSAL})
public class DavidIM implements ExampleIMSession{
	public static final String USER_NAME = DistributedTags.CLIENT_4;
	public static void main (String[] args) {
		Tracer.showInfo(true);
		CausalTracerSetter.traceCausal();
		//IMTracerSetter.traceIM();
		
		new GuiComposerAndLauncher().composeAndLaunch(SESSION_SERVER_HOST, SESSION_NAME,
				USER_NAME, Communicator.DIRECT);
	}
	
}
