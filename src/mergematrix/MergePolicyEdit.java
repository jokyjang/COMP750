package mergematrix;

import java.io.Serializable;

import trace.echo.modular.OperationName;

public class MergePolicyEdit implements Serializable{
	private OperationName server;
	private OperationName client;
	private MergePolicy policy;
	private String tracingTag;
	
	public MergePolicyEdit(OperationName a, OperationName b, MergePolicy p, String tag) {
		server = a;
		client = b;
		policy = p;
		tracingTag = tag;
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
	
	public String getTracingTag() {
		return tracingTag;
	}
	
	public void setPolicy(MergePolicy newMergePolicy) {
		policy = newMergePolicy;
	}
}
