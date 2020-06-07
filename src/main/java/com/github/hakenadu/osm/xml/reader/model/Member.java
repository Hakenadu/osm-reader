package com.github.hakenadu.osm.xml.reader.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Member {

	private String type;
	private String ref;
	private String role;
}
