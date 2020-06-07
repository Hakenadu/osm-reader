# OSM XML Reader
A light java 8+ library which allows the streaming of OSM-XML (.osm).

## Create an OsmReader
```java
final OsmReader reader = OsmReader.builder()
	.onNodeRead(node-> /* do something with node */)
	.onWayRead(way -> /* do something with way */)
	.onRelationRead(relation -> /* do something with relation */)
	.build();
```

## Read sync
```java
final OsmReadMetaData readMetaData = reader.read(getClass().getResourceAsStream("/map.osm"));
// ...
```

## Read async
```java
final CompletableFuture<OsmReadMetaData> readMetaDataFuture = reader.readAsync(getClass().getResourceAsStream("/map.osm"));
// ...
```