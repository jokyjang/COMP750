package trace.echo.modular;

import util.misc.Common;

public enum OperationName{
	ADD,
	DELETE,
	REPLACE,
	NULL;	// means no operation
	
	public static OperationName fromString(String aString) {
		return (OperationName) Common.fromString(OperationName.class, aString);
		
	}
	

}
