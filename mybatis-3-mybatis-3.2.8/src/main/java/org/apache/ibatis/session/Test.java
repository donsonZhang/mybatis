package org.apache.ibatis.session;

public class Test {

	public static void main(String[] args) {
		System.out.println("NONE: " + TransactionIsolationLevel.NONE.getLevel());
		System.out.println("READ_UNCOMMITTED: "+TransactionIsolationLevel.READ_UNCOMMITTED.getLevel());
		System.out.println("SERIALIZABLE: " + TransactionIsolationLevel.SERIALIZABLE.getLevel());

		TransactionIsolationLevel[] levels = TransactionIsolationLevel.values();
	}

}
