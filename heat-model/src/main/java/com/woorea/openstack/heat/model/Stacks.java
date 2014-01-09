package com.woorea.openstack.heat.model;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;

public class Stacks implements Iterable<Stack>, Serializable {

	@JsonProperty("stacks")
	private List<Stack> list;

	public List<Stack> getList() {
		return list;
	}

	@Override
	public Iterator<Stack> iterator() {
		return list.iterator();
	}

	@Override
	public String toString() {
		return "Stacks [list=" + list + "]";
	}
}
