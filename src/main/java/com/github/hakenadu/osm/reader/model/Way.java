package com.github.hakenadu.osm.reader.model;

import java.util.List;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class Way extends OsmParentElement {

	private List<Node> nodes;
	private List<Tag> tags;
}
