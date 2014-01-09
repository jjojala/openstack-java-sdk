package com.woorea.openstack.heat.api;

import com.woorea.openstack.base.client.HttpMethod;
import com.woorea.openstack.base.client.OpenStackClient;
import com.woorea.openstack.base.client.OpenStackRequest;
import com.woorea.openstack.heat.model.Metadata;
import com.woorea.openstack.heat.model.ResourceType;
import com.woorea.openstack.heat.model.ResourceTypes;
import com.woorea.openstack.heat.model.StackResource;
import com.woorea.openstack.heat.model.StackResources;

public class StackResourcesResource {

	private final OpenStackClient client;

	public StackResourcesResource(final OpenStackClient client) {
		this.client = client;
	}

	public List list(final String stackName, final String stackId) {
		return new List(stackName, stackId);
	}

	public Show show(final String stackName, final String stackId,
			final String resourceName) {
		return new Show(stackName, stackId, resourceName);
	}

	public ShowMetadata showMetadata(final String stackName,
		final String stackId, final String resourceName) {
		return new ShowMetadata(stackName, stackId, resourceName);
	}

	public ListResourceTypes listResourceTypes() {
		return new ListResourceTypes();
	}

	public ShowResourceType showResourceType(final String typeName) {
		return new ShowResourceType(typeName);
	}

	public class List extends OpenStackRequest<StackResources> {
		public List(final String stackName, final String stackId) {
			super(client, HttpMethod.GET,
				String.format("/stacks/%s/%s/resources",
					stackName, stackId),
				null, StackResources.class);
		}
	}

	public class Show extends OpenStackRequest<StackResource> {
		public Show(final String stackName, final String stackId,
				final String resourceName) {
			super(client, HttpMethod.GET,
				String.format("/stacks/%s/%s/resources/%s",
					stackName, stackId, resourceName),
				null, StackResource.class);
		}
	}

	public class ShowMetadata extends OpenStackRequest<Metadata> {
		public ShowMetadata(final String stackName,
				final String stackId, 
				final String resourceName) {
			super(client, HttpMethod.GET,
				String.format("/stacks/%s/%s/resources/%s/metadata",
					stackName, stackId, resourceName),
				null, Metadata.class);
		}
	}

	public class ListResourceTypes extends OpenStackRequest<ResourceTypes> {
		public ListResourceTypes() {
			super(client, HttpMethod.GET,
				"/resource_types", null, ResourceTypes.class);
		}
	}

	public class ShowResourceType extends OpenStackRequest<ResourceType> {
		public ShowResourceType(final String resourceTypeName) {
			super(client, HttpMethod.GET, String.format(
				"/resource_types/%s", resourceTypeName),
				null, ResourceType.class);
		}
	}
}
