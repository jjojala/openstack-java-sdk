package com.woorea.heat.api;

import com.woorea.heat.model.Stack;
import com.woorea.heat.model.StackForCreate;
import com.woorea.heat.model.Stacks;
import com.woorea.openstack.base.client.Entity;
import com.woorea.openstack.base.client.HttpMethod;
import com.woorea.openstack.base.client.OpenStackClient;
import com.woorea.openstack.base.client.OpenStackRequest;

public class StacksResource {

								private final OpenStackClient client;

								public StacksResource(final OpenStackClient client) {
																this.client = client;
								}

								public List list() {
																return new List();
								}

								public Show show(final String stackName, final String stackId) {
																return new Show(stackName, stackId);
								}

								public Create create(final StackForCreate stack) {
																return new Create(stack);
								}

								public Delete delete(final String stackName, final String stackId) {
																return new Delete(stackName, stackId);
								}

								public class List extends OpenStackRequest<Stacks> {

																public List() {
																								super(client, HttpMethod.GET, "/stacks/", null, Stacks.class);
																}
								}

								public class Show extends OpenStackRequest<Stack> {

																public Show(final String stackName, final String stackId) {
																								super(client, HttpMethod.GET,
																																String.format("/stacks/%s/%s", stackName, stackId),
																																null, Stack.class);
																}
								}

								public class Create extends OpenStackRequest<Stack> {

																public Create(final StackForCreate stack) {
																								super(client, HttpMethod.POST, "/stacks", Entity.json(stack), Stack.class);
																}
								}

								public class Delete extends OpenStackRequest<Void> {

																public Delete(final String stackName, final String stackId) {
																								super(client, HttpMethod.DELETE,
																																String.format("/stacks/%s/%s", stackName, stackId),
																																null, Void.class);
																}
								}
}
