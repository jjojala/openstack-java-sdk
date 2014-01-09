package com.woorea.openstack.heat.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;

public class ResourceTypes implements Iterable<ResourceType>, Serializable {

	@JsonProperty("resource_types")
	private List<ResourceType> resourceTypes;

	public final void setResourceTypes(final List<ResourceType> types) {
		this.resourceTypes = new ArrayList<ResourceType>(types);
	}

	public final List<ResourceType> getResourceTypes() {
		return resourceTypes;
	}

	@Override
	public Iterator<ResourceType> iterator() {
		return resourceTypes.iterator();
	}
}
