package com.firstorion.project.DerickMaloneVertxApp.service;

import io.vertx.ext.web.RoutingContext;

public interface BandsService {

	void getBands(RoutingContext routingContext);

	void createBand(RoutingContext routingContext);

	void updateBand(RoutingContext routingContext);

	void deleteBand(RoutingContext routingContext);

}
