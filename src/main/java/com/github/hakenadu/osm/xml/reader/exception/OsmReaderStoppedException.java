package com.github.hakenadu.osm.xml.reader.exception;

import com.github.hakenadu.osm.xml.reader.OsmReader;

/**
 * {@link OsmReaderException} which is thrown, when an {@link OsmReader} is
 * stopped but {@link OsmReader#read()} is called again.
 * 
 * @author Manuel Seiche
 * @since 07.06.2020
 */
public class OsmReaderStoppedException extends OsmReaderException {

	/**
	 * don't use this serialization mechanism
	 */
	private static final long serialVersionUID = 1337L;

	public OsmReaderStoppedException() {
		super("the OsmReader was stopped");
	}
}
