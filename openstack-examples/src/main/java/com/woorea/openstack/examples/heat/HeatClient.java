package com.woorea.openstack.examples.heat;

import com.woorea.openstack.examples.ExamplesConfiguration;
import com.woorea.openstack.heat.Heat;
import com.woorea.openstack.heat.model.Metadata;
import com.woorea.openstack.heat.model.ResourceType;
import com.woorea.openstack.heat.model.ResourceTypes;
import com.woorea.openstack.heat.model.Stack;
import com.woorea.openstack.heat.model.StackResource;
import com.woorea.openstack.heat.model.StackResources;
import com.woorea.openstack.heat.model.Stacks;
import com.woorea.openstack.keystone.Keystone;
import com.woorea.openstack.keystone.model.Access;
import com.woorea.openstack.keystone.model.authentication.UsernamePassword;
import com.woorea.openstack.keystone.utils.KeystoneUtils;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import org.codehaus.jackson.map.ObjectMapper;

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

			if ("stack-create".equals(args[1])) {
				System.exit(stackCreate(heat /* TODO: rest of the params... */));
			}

			if ("stack-update".equals(args[1])) {
				System.exit(stackUpdate(heat /* TODO: rest of the params... */));
			}

			if ("stack-delete".equals(args[1])) {
				System.exit(stackDelete(heat /* TODO: rest of the params ... */));
			}

			if ("stack-list".equals(args[1])) {
				System.exit(stackList(heat));
			}

			if ("resource-type-list".equals(args[1])) {
				System.exit(resourceTypeList(heat));
			}

			if ("resource-type-show".equals(args[1])) {
				System.exit(resourceTypeShow(heat, args[2]));
			}

			if ("stack-resource-list".equals(args[1])) {
				System.exit(stackResourceList(heat, args[2], args[3]));
			}

			if ("stack-resource-show".equals(args[1])) {
				System.exit(stackResourceShow(heat, args[2], args[3], args[4]));
			}

			if ("stack-resource-metadata-show".equals(args[1])) {
				System.exit(stackResourceMetadataShow(heat, args[2], args[3], args[4]));
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
			+ "\n\tstack-create <template-file> <stack-name> [-P<name>=<value> ...]"
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

		System.out.println("Heat endpoint: " + heatEndpoint);
		final Heat heat = new Heat(heatEndpoint);

		heat.token(access.getToken().getId());

		return heat;
	}

	public static int stackCreate(final Heat heat) {
		throw new UnsupportedOperationException("TODO: implement");
	}

	public static int stackUpdate(final Heat heat) {
		throw new UnsupportedOperationException("TODO: implement");
	}

	public static int stackDelete(final Heat heat) {
		throw new UnsupportedOperationException("TODO: implement");
	}

	public static int stackList(final Heat heat) {

		final Stacks stacks = heat.stacks().list().execute();
		for (final Stack stack : stacks.getList()) {
			System.out.println(stack);
		}

		return 0;
	}

	public static int resourceTypeList(final Heat heat) {
		final ResourceTypes types = heat.resources().listResourceTypes().execute();
		for (final String type: types.getResourceTypes()) {
			System.out.println(type);
		}

		return 0;
	}

	public static int resourceTypeShow(final Heat heat, final String resourceTypeName) {
		final ResourceType type =
			heat.resources().showResourceType(resourceTypeName).execute();
		System.out.println(type);

		return 0;
	}

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
}
