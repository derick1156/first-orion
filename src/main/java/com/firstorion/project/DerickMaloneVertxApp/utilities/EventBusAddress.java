package com.firstorion.project.DerickMaloneVertxApp.utilities;

public enum EventBusAddress {

	EVENT_BUS_ADDRESS("thirdparty.band.created", "Address that is published to when new Bands are added to Third Part Systems");

	private String address;
	private String description;

	EventBusAddress(String address, String description) {
		this.address = address;
		this.description = description;
	}

	public String getAddress() {
		return address;
	}

	public String getDescription() {
		return description;
	}
}
