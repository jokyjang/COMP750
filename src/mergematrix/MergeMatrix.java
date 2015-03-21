package mergematrix;

import trace.echo.modular.OperationName;

public interface MergeMatrix {
	
	/**
	 * 
	 * @param a
	 * @param b
	 * @param newPolicy
	 * @return the previous merge policy
	 */
	MergePolicy set(OperationName a, OperationName b, MergePolicy newPolicy);
	
	MergePolicy get(OperationName a, OperationName b);
	
	OperationName[] getAllOperations();
	
	MergePolicy[] getAllPolicies();
	
	void print();

}
