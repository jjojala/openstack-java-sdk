package com.woorea.openstack.examples.heat;

import com.woorea.openstack.heat.Heat;
import com.woorea.openstack.heat.model.Metadata;
import com.woorea.openstack.heat.model.StackResource;
import com.woorea.openstack.heat.model.StackResources;
import com.woorea.openstack.keystone.Keystone;
import com.woorea.openstack.keystone.model.Access;

public class HeatResourceClient {

	public static void main(final String[] args) {
		final Keystone keystone = HeatClient.getKeystone();
		final Access access = HeatClient.getAccess(keystone);
		final Heat heat = HeatClient.getHeat(access);

		final String stackName = System.getProperty(
			"stackName", "test-stack");

	}

	public static int list(final Heat heat, final String stackName,
			final String stackId) {
		final StackResources resources =
			heat.resources().list(stackName, stackId).execute();
		for (final StackResource resource: resources.getResources()) {
			System.out.println(resource);
		}

		return 0;
	}

	public static int show(final Heat heat, final String stackName,
			final String stackId, final String resourceName) {
		final StackResource resource =
			heat.resources().show(stackName, stackId, resourceName)
				.execute();
		System.out.println(resource);

		return 0;
	}

	public static int metadata(final Heat heat, final String stackName,
			final String stackId, final String resourceName) {
		final Metadata metadata =
			heat.resources().showMetadata(stackName, stackId,
				resourceName).execute();
		System.out.println(metadata);

		return 0;
	}

	private HeatResourceClient() {
		throw new AssertionError();
	}
}
