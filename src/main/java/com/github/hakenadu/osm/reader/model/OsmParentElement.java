package com.github.hakenadu.osm.reader.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public abstract class OsmParentElement {

	private String id;
	private boolean visible;
	private int version;
	private long changeset;
	private LocalDateTime timestamp;
	private String user;
	private long uid;
}
