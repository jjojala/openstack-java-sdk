package com.woorea.openstack.examples.heat;

import com.woorea.openstack.examples.ExamplesConfiguration;
import com.woorea.openstack.heat.Heat;
import com.woorea.openstack.keystone.Keystone;
import com.woorea.openstack.keystone.model.Access;
import com.woorea.openstack.keystone.model.authentication.UsernamePassword;
import com.woorea.openstack.keystone.utils.KeystoneUtils;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class HeatClient {

	public static void main(final String[] args) {
		try {
			final Properties props = System.getProperties();

			if (args.length > 1) {

				final Keystone keystone = getKeystone();
				final Access access = getAccess(keystone);
				final Heat heat = getHeat(access);

				if ("stack-create".equals(args[1])) {
					System.exit(HeatStacksClient.stackCreate(
						heat, readFile(args[2]), args[3],
						toMap(Arrays.copyOfRange(
							args, 4, args.length))));
				}

				if ("stack-update".equals(args[1])) {
					System.exit(HeatStacksClient.stackUpdate(
						heat /* TODO: rest of the params... */));
				}

				if ("stack-delete".equals(args[1])) {
					System.exit(HeatStacksClient.stackDelete(
						heat, args[2], args[3]));
				}

				if ("stack-list".equals(args[1])) {
					System.exit(HeatStacksClient.stackList(
						heat));
				}

				if ("resource-type-list".equals(args[1])) {
					System.exit(
						HeatResourceTypeClient.resourceTypeList(heat));
				}

				if ("resource-type-show".equals(args[1])) {
					System.exit(
						HeatResourceTypeClient.resourceTypeShow(
							heat, args[2]));
				}

				if ("stack-resource-list".equals(args[1])) {
					System.exit(
						HeatResourceClient.stackResourceList(
							heat, args[2], args[3]));
				}

				if ("stack-resource-show".equals(args[1])) {
					System.exit(
						HeatResourceClient.stackResourceShow(
							heat, args[2], args[3], args[4]));
				}

				if ("stack-resource-metadata-show".equals(args[1])) {
					System.exit(
						HeatResourceClient.stackResourceMetadataShow(
							heat, args[2], args[3], args[4]));
				}
			}

			System.out.format(
				"Usage: java [options ...] %s <subcommand> [args ...]"
				+ "\nOptions:"
				+ "\n\t-DkeystoneEndpoint=<URL>  (default: %s)"
				+ "\n\t-DkeystoneUser=<user>     (default: %s)"
				+ "\n\t-DkeystonePassword=<user> (default: %s)"
				+ "\n\t-Dtenant=<tenant>         (default: admin)"
				+ "\nSubcommands:"
				+ "\n\tstack-create <template-file> <stack-name> [<name>=<value> ...]"
				+ "\n\t                                                      create a stack"
				+ "\n\tstack-update ...                                      update a stack"
				+ "\n\tstack-delete <stack-name>                             deletes a stack"
				+ "\n\tstack-list                                            list stacks"
				+ "\n"
				+ "\n\tresource-type-list                                    list of resource types"
				+ "\n\tresource-type-show <resource-type-name>               details of resource type"
				+ "\n"
				+ "\n\tstack-resource-list <stack-name> <stack-id>           list of stack resources"
				+ "\n\tstack-resource-show <stack-name> <stack-id> <resource-name>"
				+ "\n\t                                                      details of a stack resource"
				+ "\n\tstack-resource-metadata-show <stack-name> <stack-id> <resource-name>"
				+ "\n\t                                                      metadata of a stack resource",
				args.length > 0 ? args[0] : "HeatClient",
				ExamplesConfiguration.KEYSTONE_ENDPOINT,
				ExamplesConfiguration.KEYSTONE_USERNAME,
				ExamplesConfiguration.KEYSTONE_PASSWORD);
		}

		catch (final Exception ex) {
			ex.printStackTrace(System.err);
			System.exit(1);
		}
	}

	public static String readFile(final String file) throws IOException {
		final FileReader reader = new FileReader(file);
		try {
			final StringBuilder buffer = new StringBuilder();

			final char[] slice = new char[2048];
			while (true) {
				int read = reader.read(slice);
				if (read < 1)
					return buffer.toString();

				buffer.append(slice, 0, read);
				if (read < slice.length)
					return buffer.toString();
			}
		}

		finally {
			reader.close();
		}
	}

	public static Map<String, String> toMap(final String[] args) {
		if (args == null || args.length == 0)
			return Collections.emptyMap();

		final Map<String, String> map =
			new HashMap<String, String>(args.length);
		for (final String arg: args) {
			final int separatorPos = arg.indexOf('=');
			if (separatorPos < 0)
				throw new IllegalArgumentException(
					String.format("Bad parameter '%s'",
						arg));

			map.put(arg.substring(0, separatorPos),
				arg.substring(separatorPos+1));
		}

		return map;
	}

	public static Keystone getKeystone() {
		return new Keystone(
			System.getProperty("keystoneEndpoint",
				ExamplesConfiguration.KEYSTONE_ENDPOINT));
	}

	public static Access getAccess(final Keystone keystone) {
		final String user = System.getProperty("keystoneUser",
			ExamplesConfiguration.KEYSTONE_USERNAME);
		final String password = System.getProperty("kaystonePassword",
			ExamplesConfiguration.KEYSTONE_PASSWORD);
		final String tenant = System.getProperty("tenant",
			ExamplesConfiguration.TENANT_NAME);

		return keystone.tokens().authenticate(
				new UsernamePassword(user, password))
			.withTenantName(tenant).execute();
	}

	public static Heat getHeat(final Access access) {

		// TODO: region & facing to be configurable?
		final String heatEndpoint = KeystoneUtils.findEndpointURL(
			access.getServiceCatalog(), "orchestration", "RegionOne", "public");

		System.out.println("Heat endpoint: " + heatEndpoint);
		final Heat heat = new Heat(heatEndpoint);

		heat.token(access.getToken().getId());

		return heat;
	}
}
