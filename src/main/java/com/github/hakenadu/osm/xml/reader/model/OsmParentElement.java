package com.github.hakenadu.osm.xml.reader.model;

import java.time.OffsetDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public abstract class OsmParentElement {

	private String id;
	private boolean visible;
	private int version;
	private long changeset;
	private OffsetDateTime timestamp;
	private String user;
	private String uid;

	@Singular
	private List<Tag> tags;
}
