package com.woorea.openstack.heat.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;

public class ResourceTypes implements Iterable<String>, Serializable {

	@JsonProperty("resource_types")
	private List<String> resourceTypes;

	public final void setResourceTypes(final List<String> types) {
		this.resourceTypes = new ArrayList<String>(types);
	}

	public final List<String> getResourceTypes() {
		return resourceTypes;
	}

	@Override
	public Iterator<String> iterator() {
		return resourceTypes.iterator();
	}
}
