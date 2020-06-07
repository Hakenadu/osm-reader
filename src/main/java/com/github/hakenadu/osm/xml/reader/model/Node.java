package com.github.hakenadu.osm.xml.reader.model;

import java.util.List;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class Node extends OsmParentElement {

	private double lat;
	private double lon;
	private List<Tag> tags;
}