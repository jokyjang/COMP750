package mergematrix;

import im.ExampleIMSession;
import trace.causal.CausalTracerSetter;
import trace.im.IMTracerSetter;
import trace.ot.OTTracerSetter;
import util.annotations.Tags;
import util.session.Communicator;
import util.tags.ApplicationTags;
import util.tags.DistributedTags;
import util.trace.Tracer;

@Tags({DistributedTags.CLIENT_2, DistributedTags.CAUSAL})

public class BobIM implements ExampleIMSession{
	public static final String USER_NAME = DistributedTags.CLIENT_2;
	public static void main (String[] args) {
		Tracer.showInfo(false);
		OTTracerSetter.traceOT();
		
		new GuiComposerAndLauncher().composeAndLaunch(SESSION_SERVER_HOST, SESSION_NAME,
				USER_NAME, Communicator.RELAYED);
	}
}
