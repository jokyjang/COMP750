package mergematrix;

import trace.echo.modular.OperationName;
import util.misc.Common;

public enum MergePolicy {
	SERVER,
	CLIENT,
	NONE,
	BOTH,
	NA;
	
	public static OperationName fromString(String aString) {
		return (OperationName) Common.fromString(OperationName.class, aString);
	}
}
