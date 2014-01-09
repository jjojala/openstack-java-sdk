package com.woorea.openstack.examples.heat;

import com.woorea.openstack.examples.ExamplesConfiguration;
import com.woorea.openstack.heat.Heat;
import com.woorea.openstack.heat.model.Stack;
import com.woorea.openstack.heat.model.Stacks;
import com.woorea.openstack.keystone.Keystone;
import com.woorea.openstack.keystone.model.Access;
import com.woorea.openstack.keystone.model.authentication.UsernamePassword;
import com.woorea.openstack.keystone.utils.KeystoneUtils;
import java.util.List;
import java.util.Properties;

public class HeatClient {

	public static void main(final String[] args) {

		final Properties props = System.getProperties();

		if (args.length > 1) {

			final Keystone keystone = getKeystone(
				props.getProperty("keystoneEndpoint", ExamplesConfiguration.KEYSTONE_ENDPOINT));

			final Access access = getAccess(keystone, 
				props.getProperty("keystoneUser", ExamplesConfiguration.KEYSTONE_USERNAME),
				props.getProperty("keystonePassword", ExamplesConfiguration.KEYSTONE_USERNAME),
				props.getProperty("tenant", "admin"));

			final Heat heat = getHeat(keystone, access);

			if ("stack-list".equals(args[1])) {
				System.exit(stackList(heat));
			}
			
			/* else if (...) {} */
		}

		System.out.format(
			"Usage: java [options ...] %s <subcommand> [args ...]"
			+ "\nOptions:"
			+ "\n\t-DkeystoneEndpoint=<URL>  (default: %s)"
			+ "\n\t-DkeystoneUser=<user>     (default: %s)"
			+ "\n\t-DkeystonePassword=<user> (default: %s)"
			+ "\n\t-Dtenant=<tenant>         (default: admin)"
			+ "\nSubcommands:"
			+ "\n\tstack-list                                                       - list stacks"
			+ "\n\tstack-create <template-file> <stack-name> [-P<name>=<value> ...] - create a stack"
			+ "\n\tstack-delete <stack-name>                                        - deletes a stack"
			+ "\n",
			args.length > 0 ? args[0] : "HeatClient",
			ExamplesConfiguration.KEYSTONE_ENDPOINT,
			ExamplesConfiguration.KEYSTONE_USERNAME,
			ExamplesConfiguration.KEYSTONE_PASSWORD);
	}

	public static Keystone getKeystone(final String keystoneEndpoint) {
		return new Keystone(keystoneEndpoint);
	}

	public static Access getAccess(final Keystone keystone,
			final String user, final String password, final String tenant) {
		return keystone.tokens().authenticate(
			new UsernamePassword(user, password))
			.withTenantName(tenant)
			.execute();
	}

	public static Heat getHeat(final Keystone keystone, final Access access) {

		// TODO: region & facing to be configurable?
		final String heatEndpoint = KeystoneUtils.findEndpointURL(
			access.getServiceCatalog(), "orchestration", "RegionOne", "public");

		final Heat heat = new Heat(String.format("%s/%s",
			heatEndpoint, access.getToken().getTenant().getId()));

		heat.token(access.getToken().getId());

		return heat;
	}

	public static int stackList(final Heat heat) {

		final Stacks stacks = heat.stacks().list().execute();
		for (final Stack stack: stacks.getList()) {
			System.out.println(stack);
		}

		return 0;
	}
}
