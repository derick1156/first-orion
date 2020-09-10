package com.firstorion.project.DerickMaloneVertxApp.service;

import com.firstorion.project.DerickMaloneVertxApp.domain.Band;
import io.vertx.ext.web.RoutingContext;

public interface BandsService {

	void getBands(RoutingContext routingContext);

	void createBand(RoutingContext routingContext);

	boolean createBand(Band band);

	void updateBand(RoutingContext routingContext);

	void deleteBand(RoutingContext routingContext);

}
