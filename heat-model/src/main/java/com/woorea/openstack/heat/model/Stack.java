package com.woorea.openstack.heat.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;

public class Stack implements Serializable {

	@JsonProperty("description")
	private String description;

	private List<Link> links;

	@JsonProperty("stack_status_reason")
	private String stackStatusReason;

	@JsonProperty("stack_name")
	private String stackName;

	@JsonProperty("creation_time")
	private String creationTime;

	@JsonProperty("updated_time")
	private String updatedTime;

	@JsonProperty("stack_status")
	private String stackStatus;

	@JsonProperty("id")
	private String id;

	public final void setDescription(final String description) {
		this.description = description;
	}

	public final String getDescription() {
		return description;
	}

	public final void setLinks(final List<Link> links) {
		this.links = new ArrayList<Link>(links);
	}

	public final List<Link> getLinks() {
		return links;
	}

	public final void setStackStatusReason(final String stackStatusReason) {
		this.stackStatusReason = stackStatusReason;
	}

	public final String getStackStatusReason() {
		return stackStatusReason;
	}

	public final void setStackName(final String stackName) {
		this.stackName = stackName;
	}

	public final String getStackName() {
		return stackName;
	}

	public final void setCreationTime(final String creationTime) {
		this.creationTime = creationTime;
	}

	public final String getCreationTime() {
		return creationTime;
	}

	public final void setUpdatedTime(final String updatedTime) {
		this.updatedTime = updatedTime;
	}

	public final String getUpdatedtime() {
		return updatedTime;
	}

	public final void setStackStatus(final String stackStatus) {
		this.stackStatus = stackStatus;
	}

	public final String getStackStatus() {
		return stackStatus;
	}

	public final void setId(final String id) {
		this.id = id;
	}

	public final String getId() {
		return id;
	}

	@Override
	public String toString() {
		final StringBuilder buffer = new StringBuilder();
		buffer.append(getClass().getSimpleName())
			.append(" [id='").append(id)
			.append("', stack_name='").append(stackName)
			.append("', description='").append(description)
			.append("', creation_time='").append(creationTime)
			.append("', updated_time='").append(updatedTime)
			.append("', links=[").append(links)
			.append("], stack_status='").append(stackStatus)
			.append("', stack_status_reason='")
			.append(stackStatusReason);

		return buffer.toString();
	}
}
