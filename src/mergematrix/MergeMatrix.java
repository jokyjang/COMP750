package mergematrix;

import trace.echo.modular.OperationName;

public interface MergeMatrix {
	MergePolicy set(OperationName a, OperationName b, MergePolicy newPolicy);
	MergePolicy get(OperationName a, OperationName b);
	OperationName[] getAllOperations();
	MergePolicy[] getAllPolicies();
	void print();
}
