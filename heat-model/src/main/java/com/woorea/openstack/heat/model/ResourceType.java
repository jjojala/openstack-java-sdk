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
	private Map<String, AttributeData> attributes;

	@JsonProperty("properties")
	private Map<String, PropertyData> properties;

	public final void setResourceType(final String resourceType) {
		this.resourceType = resourceType;
	}

	public final String getResourceType() {
		return resourceType;
	}

	public final void setAttributes(final Map<String, AttributeData> attributes) {
		this.attributes = new HashMap<String, AttributeData>(attributes);
	}

	public final Map<String, AttributeData> getAttributes() {
		return attributes;
	}

	public final void setProperties(
			final Map<String, PropertyData> properties) {
		this.properties = new HashMap<String, PropertyData>(properties);
	}

	public final Map<String, PropertyData> getProperties() {
		return properties;
	}

	@Override
	public String toString() {
		return "ResourceType [resource_type='" +
			resourceType + "', attributes=["
			+ attributes + "], properties=["
			+ properties + "]]";
	}

	public static class AttributeData implements Serializable {

		@JsonProperty("description")
		private String description;

		public final void setDescription(final String description) {
			this.description = description;
		}

		public final String getDescription() {
			return description;
		}

		@Override
		public String toString() {
			return "AttributeData [description='"
				+ description + "']";
		}
	}

	public static class PropertyData implements Serializable {

		@JsonProperty("required")
		private String required;

		@JsonProperty("type")
		private String type;

		@JsonProperty("schema")
		private Map<String, PropertyData> schema;

		public final void setRequired(final String required) {
			this.required = required;
		}

		public final String getRequired() {
			return required;
		}

		public final void setType(final String type) {
			this.type = type;
		}

		public final String getType() {
			return type;
		}

		public final void setSchema(final Map<String, PropertyData> schema) {
			this.schema = new HashMap<String, PropertyData>(schema);
		}

		public final Map<String, PropertyData> getSchema() {
			return schema;
		}

		@Override
		public String toString() {
			return "PropertyData [required=" + required
				+ ", type=" + type
				+ ", schema=" + schema + "]";
		}
	}
}
