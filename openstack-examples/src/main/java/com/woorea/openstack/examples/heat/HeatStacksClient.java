package com.woorea.openstack.examples.heat;

import com.woorea.openstack.heat.Heat;
import com.woorea.openstack.heat.model.Stack;
import com.woorea.openstack.heat.model.StackForCreate;
import com.woorea.openstack.heat.model.Stacks;
import com.woorea.openstack.keystone.Keystone;
import com.woorea.openstack.keystone.model.Access;
import java.util.Map;

/**
 * Example client for working with stacks.
 */
public class HeatStacksClient {

	public static void main(final String[] args) {
		try {
			final Keystone keystone = HeatClient.getKeystone();
			final Access access = HeatClient.getAccess(keystone);
			final Heat heat = HeatClient.getHeat(access);

			final String template = HeatClient.readFile(
				System.getProperty("template"));
			final String stackName = System.getProperty(
				"stackName", "test-stack");

			// Run all operations in sequence
			stackCreate(heat, template, stackName, null);
			// ...
		}

		catch (final Exception ex) {
			ex.printStackTrace(System.err);
			System.exit(1);
		}
	}

	public static int stackCreate(final Heat heat, final String template, final String stackName,
			final Map<String, String> params) {
		final StackForCreate request = new StackForCreate(
			stackName, template, null, null, params, 60);
		final Stack stack = heat.stacks().create(request).execute();
		System.out.println(stack);

		return 0;
	}

	public static int stackUpdate(final Heat heat) {
		throw new UnsupportedOperationException("TODO: implement");
	}

	public static int stackDelete(final Heat heat, final String stackName,
			final String stackId) {
		heat.stacks().delete(stackName, stackId).execute();
		return 0;
	}

	public static int stackList(final Heat heat) {

		final Stacks stacks = heat.stacks().list().execute();
		for (final Stack stack : stacks.getList()) {
			System.out.println(stack);
		}

		return 0;
	}

	private HeatStacksClient() {
		throw new AssertionError();
	}
}
