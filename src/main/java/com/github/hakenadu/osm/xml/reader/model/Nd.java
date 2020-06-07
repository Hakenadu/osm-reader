package com.github.hakenadu.osm.xml.reader.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Nd {

	/**
	 * id of a referenced {@link Node}
	 */
	private String ref;
}
