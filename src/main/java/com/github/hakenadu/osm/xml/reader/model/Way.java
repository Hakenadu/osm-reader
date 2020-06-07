package com.github.hakenadu.osm.xml.reader.model;

import java.util.List;

import lombok.Getter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class Way extends OsmParentElement {

	@Singular
	private List<Nd> nds;
}
