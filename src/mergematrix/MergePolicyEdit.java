package mergematrix;

import java.io.Serializable;

import trace.echo.modular.OperationName;

public class MergePolicyEdit implements Serializable{
	private OperationName server;
	private OperationName client;
	private MergePolicy policy;
	
	public MergePolicyEdit(OperationName a, OperationName b, MergePolicy p) {
		server = a;
		client = b;
		policy = p;
	}

	public OperationName getServer() {
		return server;
	}

	public OperationName getClient() {
		return client;
	}

	public MergePolicy getPolicy() {
		return policy;
	}
	
	public void setPolicy(MergePolicy newMergePolicy) {
		policy = newMergePolicy;
	}
}
