package com.github.hakenadu.osm.xml.reader.exception;

import com.github.hakenadu.osm.xml.reader.OsmReader;

/**
 * Superclass for all exceptions thrown by an {@link OsmReader} (for generic
 * try-catch blocks)
 * 
 * @author Manuel Seiche
 * @since 07.06.2020
 */
public abstract class OsmReaderException extends Exception {

	/**
	 * don't use this serialization mechanism
	 */
	private static final long serialVersionUID = 1337L;

	protected OsmReaderException(final String message) {
		super(message);
	}

	protected OsmReaderException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
