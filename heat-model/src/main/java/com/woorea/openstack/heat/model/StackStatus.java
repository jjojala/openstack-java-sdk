package com.woorea.openstack.heat.model;

public final class StackStatus {

	public static final String CREATE_IN_PROGRESS = "CREATE_IN_PROGRESS";
	public static final String CREATE_COMPLETE = "CREATE_COMPLETE";
	public static final String CREATE_FAILED = "CREATE_FAILED";

	public static final String UPDATE_IN_PROGRESS = "UPDATE_IN_PROGRESS";
	public static final String UPDATE_COMPLETE = "UPDATE_COMPLETE";
	public static final String UPDATE_FAILED = "UPDATE_FAILED";

	public static final String DELETE_IN_PROGRESS = "DELETE_IN_PROGRESS";
	public static final String DELETE_FAILED = "DELETE_FAILED";
		
	private StackStatus() {
		throw new AssertionError("Non-instantiable class!");
	}
}
