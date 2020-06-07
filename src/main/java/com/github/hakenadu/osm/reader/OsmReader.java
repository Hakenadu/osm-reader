package com.github.hakenadu.osm.reader;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import com.github.hakenadu.osm.reader.exception.OsmReaderStoppedException;
import com.github.hakenadu.osm.reader.model.Node;
import com.github.hakenadu.osm.reader.model.Way;

import lombok.Builder;
import lombok.Getter;

/**
 * Concrete {@link OsmReader} implementation
 * 
 * @author Manuel Seiche
 * @since 07.06.2020
 */
@Builder
@Getter
public final class OsmReader {

	/**
	 * is called when a {@link Node} was read
	 */
	private Consumer<Node> onNodeRead;

	/**
	 * is called when a {@link Way} was read
	 */
	private Consumer<Way> onWayRead;

	/**
	 * stops the streaming of data and releases memory
	 * 
	 * @return {@link OsmReadMetaData} of the stopped run
	 */
	public OsmReadMetaData stop() {
		return null;
	}

	/**
	 * pauses the streaming of data until it is continued by calling {@link #read()}
	 * again
	 * 
	 * @return {@link OsmReadMetaData} for the paused run
	 */
	public OsmReadMetaData pause() {
		return null;
	}

	/**
	 * @return {@link OsmReadMetaData} for the finished run
	 * @throws OsmReaderStoppedException if {@link #stop()} was already called
	 */
	public OsmReadMetaData read() throws OsmReaderStoppedException {
		return null;
	}

	/**
	 * @return {@link CompletableFuture} which provides the {@link OsmReadMetaData}
	 *         for the finished run
	 * @throws OsmReaderStoppedException if {@link #stop()} was already called
	 */
	public CompletableFuture<OsmReadMetaData> readAsync() throws OsmReaderStoppedException {
		return null;
	}
}