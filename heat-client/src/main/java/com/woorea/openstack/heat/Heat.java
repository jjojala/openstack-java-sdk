package com.woorea.openstack.heat;

import com.woorea.openstack.heat.api.StacksResource;
import com.woorea.openstack.base.client.OpenStackClient;
import com.woorea.openstack.base.client.OpenStackClientConnector;
import com.woorea.openstack.heat.api.StackResourcesResource;

public class Heat extends OpenStackClient {

	public Heat(String endpoint, OpenStackClientConnector connector) {
		super(endpoint, connector);

		STACKS = new StacksResource(this);
		STACK_RESOURCES = new StackResourcesResource(this);
	}

	public Heat(String endpoint) {
		this(endpoint, null);
	}

	public StacksResource stacks() {
		return STACKS;
	}

	public StackResourcesResource resources() {
		return STACK_RESOURCES;
	}

	private final StacksResource STACKS;
	private final StackResourcesResource STACK_RESOURCES;
}
