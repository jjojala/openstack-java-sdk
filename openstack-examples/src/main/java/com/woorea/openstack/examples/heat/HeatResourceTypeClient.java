package com.woorea.openstack.examples.heat;

import com.woorea.openstack.heat.Heat;
import com.woorea.openstack.heat.model.ResourceType;
import com.woorea.openstack.heat.model.ResourceTypes;

/**
 * Example client for resource type objects in heat.
 */
public class HeatResourceTypeClient {


	public static int list(final Heat heat) {
		final ResourceTypes types = heat.resources().listResourceTypes().execute();
		for (final String type: types.getResourceTypes()) {
			System.out.println(type);
		}

		return 0;
	}

	public static int show(final Heat heat, final String resourceTypeName) {
		final ResourceType type =
			heat.resources().showResourceType(resourceTypeName).execute();
		System.out.println(type);

		return 0;
	}

	private HeatResourceTypeClient() {
		throw new AssertionError();
	}
}
