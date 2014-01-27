package com.woorea.openstack.heat.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;

public class StackResource implements Serializable {

	@JsonProperty("resource_name")
	private String resourceName;

	@JsonProperty("description")
	private String description;

	@JsonProperty("links")
	private List<Link> links;

	@JsonProperty("logical_resource_id")
	private String logicalResourceId;

	@JsonProperty("resource_status")
	private String resourceStatus;

	@JsonProperty("updated_time")
	private String updatedTime;

	@JsonProperty("required_by")
	private List<String> requiredBy;

	@JsonProperty("resource_status_reason")
	private String resourceStatusReason;

	@JsonProperty("physical_resource_id")
	private String physicalResourceId;

	@JsonProperty("resource_type")
	private String resourceType;

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(final String resourceName) {
		this.resourceName = resourceName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(final List<Link> links) {
		this.links = new ArrayList<Link>(links);
	}

	public String getLogicalResourceId() {
		return logicalResourceId;
	}

	public void setLogicalResourceId(final String logicalResourceId) {
		this.logicalResourceId = logicalResourceId;
	}

	public String getResourceStatus() {
		return resourceStatus;
	}

	public void setResourceStatus(final String resourceStatus) {
		this.resourceStatus = resourceStatus;
	}

	public String getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(final String updatedTime) {
		this.updatedTime = updatedTime;
	}

	public List<String> getRequiredBy() {
		return requiredBy;
	}

	public void setRequiredBy(final List<String> requiredBy) {
		this.requiredBy = new ArrayList<String>(requiredBy);
	}

	public String getResourceStatusReason() {
		return resourceStatusReason;
	}

	public void setResourceStatusReason(final String resourceStatusReason) {
		this.resourceStatusReason = resourceStatusReason;
	}

	public String getPhysicalResourceId() {
		return physicalResourceId;
	}

	public void setPhysicalResourceId(final String physicalResourceId) {
		this.physicalResourceId = physicalResourceId;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(final String resourceType) {
		this.resourceType = resourceType;
	}

	@Override
	public String toString() {
		return "StackResource [resource_name='"
			+ resourceName + "', description='"
			+ description + "', logical_resource_id='"
			+ logicalResourceId + "', physical_resource_id='"
			+ physicalResourceId + "', ...]";
	}
}
