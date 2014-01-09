package com.woorea.heat.model;

import java.io.Serializable;

public class Template implements Serializable {

								@Override
								public String toString() {
																final StringBuilder buffer = new StringBuilder();
																buffer.append(getClass().getSimpleName())
																								.append(" []");

																return buffer.toString();
								}
}
