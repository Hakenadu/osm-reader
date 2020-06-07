package com.github.hakenadu.osm.xml.reader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.concurrent.CompletableFuture;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;

import com.github.hakenadu.osm.xml.reader.model.OsmParentElement;

/**
 * Tests for the {@link OsmReader} class
 * 
 * @author Manuel Seiche
 * @since 07.06.2020
 */
public class OsmReaderTest {

	@Test
	public void testRead() throws XMLStreamException {
		final OsmReader reader = OsmReader.builder().onNodeRead(this::assertParentElement)
				.onRelationRead(this::assertParentElement).onWayRead(this::assertParentElement).build();
		final OsmReadMetaData readMetaData = reader.read(getClass().getResourceAsStream("/map.osm"));
		assertMapOsmMetaData(readMetaData);
	}

	@Test
	public void testReadAsync() throws XMLStreamException, FactoryConfigurationError {
		final OsmReader reader = OsmReader.builder().onNodeRead(this::assertParentElement)
				.onRelationRead(this::assertParentElement).onWayRead(this::assertParentElement).build();
		final CompletableFuture<OsmReadMetaData> readMetaDataFuture = reader
				.readAsync(getClass().getResourceAsStream("/map.osm"));
		Awaitility.await().until(readMetaDataFuture::isDone);
		assertMapOsmMetaData(readMetaDataFuture.getNow(null));
	}

	private void assertMapOsmMetaData(final OsmReadMetaData readMetaData) {
		assertNotNull(readMetaData, "no OsmReadMetaData retrieved");
		assertEquals(5146, readMetaData.getNodeCount(), "wrong node count");
		assertEquals(756, readMetaData.getWayCount(), "wrong way count");
		assertEquals(129, readMetaData.getRelationCount(), "wrong relation count");
	}

	private void assertParentElement(final OsmParentElement parentElement) {
		assertNotNull(parentElement, "no element passed");
		assertNotNull(parentElement.getId(), "no id read");
		assertNotNull(parentElement.getTimestamp(), "no timestamp read");
		assertNotNull(parentElement.getUid(), "no uid read");
		assertNotNull(parentElement.getUser(), "no user read");
	}
}
