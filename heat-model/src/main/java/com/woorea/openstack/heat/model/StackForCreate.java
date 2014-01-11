package com.woorea.openstack.heat.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.codehaus.jackson.annotate.JsonProperty;

public class StackForCreate implements Serializable {

	@JsonProperty("stack_name")
	private String stackName;
	@JsonProperty("template")
	private String template;
	// 'template_url' is exclusive alternative for 'template' - not supported
	// private String templateUrl 
	@JsonProperty("environment")
	private String environment;
	private Map<String, String> files;
	private Map<String, String> parameters;
	@JsonProperty("timeout_mins")
	private String timeoutMins;

	public StackForCreate(final String stackName, final String template,
		final String environment, final Map<String, String> files,
		final Map<String, String> parameters, final long timeoutMins) {
		this.stackName = stackName;
		this.template = template;
		this.environment = environment;
		setFiles(files);
		setParameters(parameters);
		this.timeoutMins = Long.toString(timeoutMins, 10 /* radix for decimal number */);
	}

	public final String getStackName() {
		return stackName;
	}

	public final void setName(final String stackName) {
		this.stackName = stackName;
	}

	public final String getTemplate() {
		return template;
	}

	public final void setTemplate(final String template) {
		this.template = template;
	}

	public final String getEnvironment() {
		return environment;
	}

	public final void setEnvironment(final String environment) {
		this.environment = environment;
	}

	public final Map<String, String> getFiles() {
		return files;
	}

	public final void setFiles(final Map<String, String> files) {
		this.files = files != null
			? new HashMap<String, String>(files)
			: Collections.<String, String>emptyMap();
	}

	public final Map<String, String> getParameters() {
		return parameters;
	}

	public final void setParameters(final Map<String, String> parameters) {
		this.parameters = parameters != null
			? new HashMap<String, String>(parameters)
			: Collections.<String, String>emptyMap();
	}

	public final String getTimeout() {
		return timeoutMins;
	}

	public final void setTimeout(final String timeoutMins) {
		Long.parseLong(timeoutMins, 10 /* radix */); // validates the value
		this.timeoutMins = timeoutMins;
	}

	@Override
	public final String toString() {
		final StringBuilder buffer = new StringBuilder();
		buffer.append(getClass().getSimpleName())
			.append(" [stack_name='").append(stackName)
			.append("', template='");
		trunc(buffer, template, 160)
			.append("', environment='")
			.append(environment)
			.append("files=[");
		append(buffer, files)
			.append("], parameters=[");
		append(buffer, parameters)
			.append("], timeout_mins=")
			.append(timeoutMins);

		return buffer.toString();
	}

	private static StringBuilder trunc(final StringBuilder buffer,
		final String source, int maxLenght) {
		if (source.length() > maxLenght) {
			return buffer.append(source.substring(maxLenght))
				.append(" ...");
		}

		return buffer.append(source);
	}

	private static StringBuilder append(final StringBuilder buffer,
		final Map<String, String> map) {
		if (map != null) {
			for (final Entry<String, String> e : map.entrySet()) {
				buffer.append("{'").append(e.getKey())
					.append("': '").append(e.getValue())
					.append("'}");
			}
		}

		return buffer;
	}
}
