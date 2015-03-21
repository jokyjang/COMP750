package mergematrix;

import java.util.HashMap;

import trace.echo.modular.OperationName;

public class SimpleListMergeMatrix implements MergeMatrix {
	private HashMap<OperationName, HashMap<OperationName, MergePolicy>> matrix = 
			new HashMap<OperationName, HashMap<OperationName, MergePolicy>>();
	private OperationName[] ops = {OperationName.ADD, OperationName.DELETE, OperationName.REPLACE};
	private MergePolicy[] policies = {MergePolicy.BOTH, MergePolicy.CLIENT, MergePolicy.SERVER, MergePolicy.NONE};
	
	public SimpleListMergeMatrix() {
		put(OperationName.ADD, OperationName.ADD, MergePolicy.BOTH);
		put(OperationName.ADD, OperationName.DELETE, MergePolicy.BOTH);
		put(OperationName.ADD, OperationName.REPLACE, MergePolicy.BOTH);
		put(OperationName.DELETE, OperationName.ADD, MergePolicy.BOTH);
		put(OperationName.DELETE, OperationName.DELETE, MergePolicy.NONE);
		put(OperationName.DELETE, OperationName.REPLACE, MergePolicy.SERVER);
		put(OperationName.REPLACE, OperationName.ADD, MergePolicy.BOTH);
		put(OperationName.REPLACE, OperationName.DELETE, MergePolicy.CLIENT);
		put(OperationName.REPLACE, OperationName.REPLACE, MergePolicy.SERVER);
	}
	
	private MergePolicy put(OperationName a, OperationName b,
			MergePolicy newPolicy) {
		// TODO Auto-generated method stub
		MergePolicy previousPolicy = null;
		HashMap<OperationName, MergePolicy> row = matrix.get(a);
		if(row == null) {
			row = new HashMap<OperationName, MergePolicy>();
			row.put(b, newPolicy);
			matrix.put(a, row);
		} else {
			previousPolicy = row.put(b, newPolicy);
		}
		return previousPolicy;
	}

	@Override
	public MergePolicy set(OperationName a, OperationName b,
			MergePolicy newPolicy) {
		// TODO Auto-generated method stub
		HashMap<OperationName, MergePolicy> row = matrix.get(a);
		if(row == null) return null;
		if (row.get(b) == null) return null;
		return row.put(b, newPolicy);
	}

	@Override
	public MergePolicy get(OperationName a, OperationName b) {
		// TODO Auto-generated method stub
		HashMap<OperationName, MergePolicy> row = matrix.get(a);
		return row == null ? null : row.get(b);
	}

	@Override
	public OperationName[] getAllOperations() {
		// TODO Auto-generated method stub
		return ops;
	}

	@Override
	public MergePolicy[] getAllPolicies() {
		// TODO Auto-generated method stub
		return policies;
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub
		OperationName[] operations = getAllOperations();
		for(int i = 0; i < operations.length; ++i) {
			System.out.print("\t" + operations[i]);
		}
		System.out.println();
		for(int i = 0; i < operations.length; ++i) {
			System.out.print(operations[i]);
			for(int j = 0; j < operations.length; ++j) {
				System.out.print("\t" + get(operations[i], operations[j]));
			}
			System.out.println();
		}
	}

}
