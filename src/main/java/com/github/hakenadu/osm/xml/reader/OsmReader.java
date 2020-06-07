package com.github.hakenadu.osm.xml.reader;

import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import javax.xml.namespace.QName;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import com.github.hakenadu.osm.xml.reader.model.Bounds;
import com.github.hakenadu.osm.xml.reader.model.Member;
import com.github.hakenadu.osm.xml.reader.model.Nd;
import com.github.hakenadu.osm.xml.reader.model.Node;
import com.github.hakenadu.osm.xml.reader.model.Node.NodeBuilder;
import com.github.hakenadu.osm.xml.reader.model.OsmParentElement.OsmParentElementBuilder;
import com.github.hakenadu.osm.xml.reader.model.Relation;
import com.github.hakenadu.osm.xml.reader.model.Relation.RelationBuilder;
import com.github.hakenadu.osm.xml.reader.model.Tag;
import com.github.hakenadu.osm.xml.reader.model.Way;
import com.github.hakenadu.osm.xml.reader.model.Way.WayBuilder;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Concrete {@link OsmReader} implementation
 * 
 * @author Manuel Seiche
 * @since 07.06.2020
 */
@Builder
public final class OsmReader {

	private enum OsmParentElementType {
		NODE, WAY, RELATION;
	}

	@Getter
	@Setter
	private static class RuntimeVariables {
		private XMLEventReader xmlEventReader;
		private OsmReadMetaData metaData;

		private NodeBuilder<?, ?> node;
		private WayBuilder<?, ?> way;
		private RelationBuilder<?, ?> relation;

		private OsmParentElementType parentElementType;

		public void addTag(final Tag tag) {
			switch (parentElementType) {
			case NODE:
				this.node.tag(tag);
				break;
			case WAY:
				this.way.tag(tag);
				break;
			case RELATION:
				this.relation.tag(tag);
				break;
			default:
				throw new IllegalStateException("no parentElementType set");
			}
		}
	}

	/**
	 * is called when the {@link Bounds} were read
	 */
	private Consumer<Bounds> onBoundsRead;

	/**
	 * is called when a {@link Node} was read
	 */
	private Consumer<Node> onNodeRead;

	/**
	 * is called when a {@link Way} was read
	 */
	private Consumer<Way> onWayRead;

	/**
	 * is called when a {@link Relation} was read
	 */
	private Consumer<Relation> onRelationRead;

	private final RuntimeVariables runtimeVariables = new RuntimeVariables();

	/**
	 * @param inputStream .osm-{@link InputStream}
	 * @return {@link OsmReadMetaData} for the finished run
	 * @throws OsmReaderStoppedException if {@link #stop()} was already called
	 * @throws XMLStreamException
	 */
	public OsmReadMetaData read(final InputStream inputStream) throws XMLStreamException {
		if (runtimeVariables.getXmlEventReader() != null) {
			throw new IllegalStateException("read was already called once on this OsmReader");
		}

		initRuntimeVariables(inputStream);
		read();

		return runtimeVariables.getMetaData();
	}

	/**
	 * @param inputStream .osm-{@link InputStream}
	 * @return {@link CompletableFuture} which provides the {@link OsmReadMetaData}
	 *         for the finished run
	 * @throws FactoryConfigurationError
	 * @throws XMLStreamException
	 */
	public CompletableFuture<OsmReadMetaData> readAsync(final InputStream inputStream)
			throws XMLStreamException, FactoryConfigurationError {
		final CompletableFuture<OsmReadMetaData> future = new CompletableFuture<>();
		initRuntimeVariables(inputStream);
		Executors.newCachedThreadPool().submit(() -> {
			try {
				read();
				future.complete(runtimeVariables.metaData);
			} catch (final XMLStreamException xmlStreamException) {
				throw new IllegalStateException("exception occurred while completing future", xmlStreamException);
			}
		});
		return future;
	}

	private void read() throws XMLStreamException {
		while (runtimeVariables.getXmlEventReader().hasNext()) {
			final XMLEvent event = runtimeVariables.getXmlEventReader().nextEvent();
			if (event.isStartElement()) {
				processStartElement(event.asStartElement());
			} else if (event.isEndElement()) {
				processEndElement(event.asEndElement());
			}
		}
		runtimeVariables.getXmlEventReader().close();
	}

	private void initRuntimeVariables(final InputStream inputStream)
			throws XMLStreamException, FactoryConfigurationError {
		runtimeVariables.setXmlEventReader(XMLInputFactory.newInstance().createXMLEventReader(inputStream));
		runtimeVariables.setMetaData(new OsmReadMetaData());
	}

	private <T extends OsmParentElementBuilder<?, ?>> T initBuilder(final T builder, final StartElement startElement) {
		Optional.ofNullable(startElement.getAttributeByName(new QName("id"))).map(Attribute::getValue)
				.ifPresent(builder::id);
		Optional.ofNullable(startElement.getAttributeByName(new QName("visible"))).map(Attribute::getValue)
				.map(Boolean::parseBoolean).ifPresent(builder::visible);
		Optional.ofNullable(startElement.getAttributeByName(new QName("version"))).map(Attribute::getValue)
				.map(Integer::parseInt).ifPresent(builder::version);
		Optional.ofNullable(startElement.getAttributeByName(new QName("changeset"))).map(Attribute::getValue)
				.map(Long::parseLong).ifPresent(builder::changeset);
		Optional.ofNullable(startElement.getAttributeByName(new QName("timestamp"))).map(Attribute::getValue)
				.map(OffsetDateTime::parse).ifPresent(builder::timestamp);
		Optional.ofNullable(startElement.getAttributeByName(new QName("user"))).map(Attribute::getValue)
				.ifPresent(builder::user);
		Optional.ofNullable(startElement.getAttributeByName(new QName("uid"))).map(Attribute::getValue)
				.ifPresent(builder::uid);
		return builder;
	}

	private void processStartElement(final StartElement startElement) {
		final String name = startElement.getName().getLocalPart();
		switch (name) {
		case "node":
			runtimeVariables.setParentElementType(OsmParentElementType.NODE);
			final NodeBuilder<?, ?> nodeBuilder = initBuilder(Node.builder(), startElement);
			Optional.ofNullable(startElement.getAttributeByName(new QName("lat"))).map(Attribute::getValue)
					.map(Double::parseDouble).ifPresent(nodeBuilder::lat);
			Optional.ofNullable(startElement.getAttributeByName(new QName("lon"))).map(Attribute::getValue)
					.map(Double::parseDouble).ifPresent(nodeBuilder::lon);
			runtimeVariables.setNode(nodeBuilder);
			break;
		case "way":
			runtimeVariables.setParentElementType(OsmParentElementType.WAY);
			runtimeVariables.setWay(initBuilder(Way.builder(), startElement));
			break;
		case "relation":
			runtimeVariables.setParentElementType(OsmParentElementType.RELATION);
			runtimeVariables.setRelation(initBuilder(Relation.builder(), startElement));
			break;
		case "tag":
			final String k = startElement.getAttributeByName(new QName("k")).getValue();
			final String v = startElement.getAttributeByName(new QName("v")).getValue();
			runtimeVariables.addTag(new Tag(k, v));
			break;
		case "nd":
			final String ndRef = startElement.getAttributeByName(new QName("ref")).getValue();
			runtimeVariables.getWay().nd(new Nd(ndRef));
			break;
		case "member":
			final String type = startElement.getAttributeByName(new QName("type")).getValue();
			final String ref = startElement.getAttributeByName(new QName("ref")).getValue();
			final String role = startElement.getAttributeByName(new QName("role")).getValue();
			runtimeVariables.getRelation().member(new Member(type, ref, role));
			break;
		case "bounds":
			final String minlat = startElement.getAttributeByName(new QName("minlat")).getValue();
			final String minlon = startElement.getAttributeByName(new QName("minlon")).getValue();
			final String maxlat = startElement.getAttributeByName(new QName("maxlat")).getValue();
			final String maxlon = startElement.getAttributeByName(new QName("maxlon")).getValue();
			if (onBoundsRead != null) {
				onBoundsRead.accept(new Bounds(Double.parseDouble(minlat), Double.parseDouble(minlon),
						Double.parseDouble(maxlat), Double.parseDouble(maxlon)));
			}
			break;
		default:
			break;
		}
	}

	private void processEndElement(final EndElement endElement) {
		final String name = endElement.getName().getLocalPart();
		switch (name) {
		case "node":
			if (onNodeRead != null) {
				onNodeRead.accept(runtimeVariables.getNode().build());
			}
			runtimeVariables.getMetaData().setNodeCount(runtimeVariables.getMetaData().getNodeCount() + 1);
			break;
		case "way":
			if (onWayRead != null) {
				onWayRead.accept(runtimeVariables.getWay().build());
			}
			runtimeVariables.getMetaData().setWayCount(runtimeVariables.getMetaData().getWayCount() + 1);
			break;
		case "relation":
			if (onRelationRead != null) {
				onRelationRead.accept(runtimeVariables.getRelation().build());
			}
			runtimeVariables.getMetaData().setRelationCount(runtimeVariables.getMetaData().getRelationCount() + 1);
			break;
		default:
			break;
		}
	}
}