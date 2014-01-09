package com.woorea.openstack.heat.model;

import java.io.Serializable;

public class Link implements Serializable {

	private String rel;
	private String href;
	private String type;

	public String getRel() {
		return rel;
	}

	public String getHref() {
		return href;
	}

	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return "Link [rel=" + rel + ", href=" + href + ", type=" + type + "]";
	}
}
