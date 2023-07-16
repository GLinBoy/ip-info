package com.glinboy.ip.address.model.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum ResponseType {
	HTML, JSON;

	static Map<String, ResponseType> lookup = Arrays.stream(values())
			.collect(Collectors.toMap(a -> a.name().toLowerCase(), a -> a));

	public static ResponseType lookup(String name) {
		return lookup.get(name.toLowerCase());
	}
}
