package com.github.hakenadu.osm.xml.reader.model;

import java.util.List;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class Way extends OsmParentElement {

	private List<Nd> nds;
	private List<Tag> tags;
}
