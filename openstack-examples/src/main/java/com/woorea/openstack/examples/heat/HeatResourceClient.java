package com.woorea.openstack.examples.heat;

import com.woorea.openstack.heat.Heat;
import com.woorea.openstack.heat.model.Metadata;
import com.woorea.openstack.heat.model.StackResource;
import com.woorea.openstack.heat.model.StackResources;

public class HeatResourceClient {

	public static int stackResourceList(final Heat heat, final String stackName,
			final String stackId) {
		final StackResources resources =
			heat.resources().list(stackName, stackId).execute();
		for (final StackResource resource: resources.getResources()) {
			System.out.println(resource);
		}

		return 0;
	}

	public static int stackResourceShow(final Heat heat, final String stackName,
			final String stackId, final String resourceName) {
		final StackResource resource =
			heat.resources().show(stackName, stackId, resourceName).execute();
		System.out.println(resource);

		return 0;
	}

	public static int stackResourceMetadataShow(final Heat heat, final String stackName,
			final String stackId, final String resourceName) {
		final Metadata metadata =
			heat.resources().showMetadata(stackName, stackId, resourceName).execute();
		System.out.println(metadata);

		return 0;
	}

	private HeatResourceClient() {
		throw new AssertionError();
	}
}
