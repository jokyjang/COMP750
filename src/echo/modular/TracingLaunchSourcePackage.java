package echo.modular;

import trace.echo.ListEditMade;
import trace.echo.modular.EchoTracerSetter;
import trace.echo.modular.ListEditNotified;
import trace.echo.modular.ListEditObserved;
import util.trace.ImplicitKeywordKind;
import util.trace.TraceableInfo;
import util.trace.Tracer;

public class TracingLaunchSourcePackage {
	public static void main (String[] args) {
		Tracer.showInfo(true);
		Tracer.setImplicitPrintKeywordKind(ImplicitKeywordKind.OBJECT_PACKAGE_NAME);
		TraceableInfo.setPrintTraceable(true);
		TraceableInfo.setPrintSource(true);
		TraceableInfo.setPrintTime(false);
		TraceableInfo.setPrintThread(false);
		Tracer.setKeywordPrintStatus(ASimpleList.class, true);
		AnEchoComposerAndLauncher.traceUnawareLaunch(args);
	}

}
