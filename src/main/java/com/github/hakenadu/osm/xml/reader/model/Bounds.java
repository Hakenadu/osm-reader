package com.github.hakenadu.osm.xml.reader.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Bounds {

	private double minlat;
	private double minlon;
	private double maxlat;
	private double maxlon;
}
