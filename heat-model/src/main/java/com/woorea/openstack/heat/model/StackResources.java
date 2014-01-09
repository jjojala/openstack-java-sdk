package com.woorea.openstack.heat.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;

public class StackResources implements Iterable<StackResource>, Serializable {

	@JsonProperty("resources")
	private List<StackResource> resources;

	public final void setResources(final List<StackResource> resources) {
		this.resources = new ArrayList<StackResource>(resources);
	}

	public final List<StackResource> getResources() {
		return resources;
	}

	@Override
	public Iterator<StackResource> iterator() {
		return resources.iterator();
	}

	@Override
	public String toString() {
		return "StackResources [" + resources + "]";
	}
}
