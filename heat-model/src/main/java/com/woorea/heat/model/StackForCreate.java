package com.woorea.heat.model;

import java.io.Serializable;
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
																this.files = new HashMap<String, String>(files);
																this.parameters = new HashMap<String, String>(parameters);
																this.timeoutMins = Long.toString(timeoutMins, 10 /* radix for decimal number */);
								}

								public String getStackName() {
																return stackName;
								}

								public void setName(final String stackName) {
																this.stackName = stackName;
								}

								public String getTemplate() {
																return template;
								}

								public void setTemplate(final String template) {
																this.template = template;
								}

								public String getEnvironment() {
																return environment;
								}

								public void setEnvironment(final String environment) {
																this.environment = environment;
								}

								public Map<String, String> getFiles() {
																return files;
								}

								public void setFiles(final Map<String, String> files) {
																this.files = files;
								}

								public Map<String, String> getParameters() {
																return parameters;
								}

								public void setParameters(final Map<String, String> parameters) {
																this.parameters = parameters;
								}

								public String getTimeout() {
																return timeoutMins;
								}

								public void setTimeout(final String timeoutMins) {
																Long.parseLong(timeoutMins, 10 /* radix */); // validates the value
																this.timeoutMins = timeoutMins;
								}

								@Override
								public String toString() {
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
																if (source.length() > maxLenght)
																								return buffer.append(source.substring(maxLenght))
																																.append(" ...");

																return buffer.append(source);
								}

								private static StringBuilder append(final StringBuilder buffer,
																								final Map<String, String> map) {
																if (map != null) {
																								for (final Entry<String, String> e: map.entrySet()) {
																																buffer.append("{'").append(e.getKey())
																																								.append("': '").append(e.getValue())
																																								.append("'}");
																								}
																}

																return buffer;
								}
}
