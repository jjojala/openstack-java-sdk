package com.woorea.openstack.connector;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.woorea.openstack.base.client.HttpMethod;
import com.woorea.openstack.base.client.OpenStackClientConnector;
import com.woorea.openstack.base.client.OpenStackRequest;
import com.woorea.openstack.base.client.OpenStackResponse;
import com.woorea.openstack.base.client.OpenStackResponseException;

public class JaxRs20Connector implements OpenStackClientConnector {

	protected Client client = OpenStack.CLIENT;
	private Jersey2LoggingFilter logger = new Jersey2LoggingFilter(Logger.getLogger("os"), 10000);

	public JaxRs20Connector() {
		client.register(logger);
	}

	@Override
	public <T> OpenStackResponse request(OpenStackRequest<T> request) {
		WebTarget target = client.target(request.endpoint()).path(request.path());

		for(Map.Entry<String, List<Object> > entry : request.queryParams().entrySet()) {
			for (Object o : entry.getValue()) {
				target = target.queryParam(entry.getKey(), o);
			}
		}

		Invocation.Builder invocation = target.request();

		for(Map.Entry<String, List<Object>> h : request.headers().entrySet()) {
			StringBuilder sb = new StringBuilder();
			for(Object v : h.getValue()) {
				sb.append(String.valueOf(v));
			}
			invocation.header(h.getKey(), sb);
		}

		Entity<?> entity = (request.entity() == null) ? null :
				Entity.entity(request.entity().getEntity(), request.entity().getContentType());

		try {
			if (entity != null) {
				return new JaxRs20Response(invocation.method(request.method().name(), entity));
			} else {
				if(HttpMethod.PUT == request.method()) {
					return new JaxRs20Response(invocation.method(request.method().name(), Entity.entity("", MediaType.APPLICATION_JSON)));
				} else {
					return new JaxRs20Response(invocation.method(request.method().name()));
				}
			}
		} catch (ClientErrorException e) {
			throw new OpenStackResponseException(e.getResponse()
					.getStatusInfo().toString(), e.getResponse().getStatus());
		}
	}
}
