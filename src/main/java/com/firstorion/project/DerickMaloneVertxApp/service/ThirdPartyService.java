package com.firstorion.project.DerickMaloneVertxApp.service;

import io.vertx.ext.web.RoutingContext;

public interface ThirdPartyService {

	void getThirdPartyBands(RoutingContext routingContext);

	void createBandInThirdPartyRepository(RoutingContext routingContext);

}
