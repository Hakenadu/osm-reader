package com.github.hakenadu.osm.reader;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Information about a completed {@link OsmReader} run
 * 
 * @author Manuel Seiche
 * @since 07.06.2020
 */
@Getter
@Setter(AccessLevel.PACKAGE)
public class OsmReadMetaData {

	/**
	 * number of read relations
	 */
	private int relationCount;

	/**
	 * number of read nodes
	 */
	private int nodeCount;

	/**
	 * number of read ways
	 */
	private int wayCount;
}
