package com.woorea.openstack.heat.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.codehaus.jackson.annotate.JsonProperty;

public class ResourceType implements Serializable {

	@JsonProperty("resource_type")
	private String resourceType;

	@JsonProperty("attributes")
	private Map<String, Map<String, String>> attributes;

	@JsonProperty("properties")
	private Map<String, Map<String, String>> properties;

	public final void setResourceType(final String resourceType) {
		this.resourceType = resourceType;
	}

	public final String getResourceType() {
		return resourceType;
	}

	public final void setAttributes(
			final Map<String, Map<String, String>> attributes) {
		this.attributes = copy(attributes);
	}

	public final Map<String, Map<String, String>> getAttributes() {
		return attributes;
	}

	public final void setProperties(
			final Map<String, Map<String, String>> properties) {
		this.properties = copy(properties);
	}

	public final Map<String, Map<String, String>> getProperties() {
		return properties;
	}

	@Override
	public String toString() {
		return "ResourceType [resource_type='" +
			resourceType + "', attributes=["
			+ attributes + "], properties=["
			+ properties + "]]";
	}

	private static Map<String, Map<String, String>> copy(
			final Map<String, Map<String, String>> source) {
		if (source == null || source.isEmpty())
			return new HashMap<String, Map<String, String>>();

		final Map<String, Map<String, String>> target =
			new HashMap<String, Map<String, String>>(source.size());
		for (final Entry<String, Map<String, String>> e:
				source.entrySet()) {
			target.put(e.getKey(),
				new HashMap<String, String>(e.getValue()));
		}

		return target;
	}
}
