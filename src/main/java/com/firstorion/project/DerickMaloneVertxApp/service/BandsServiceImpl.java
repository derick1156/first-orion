package com.firstorion.project.DerickMaloneVertxApp.service;

import com.firstorion.project.DerickMaloneVertxApp.domain.Band;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

import java.util.LinkedHashMap;
import java.util.Map;

public class BandsServiceImpl implements BandsService {

	static Map<Integer, Band> bands = new LinkedHashMap<>();

	@Override
	public void getBands(RoutingContext routingContext) {
		String id = routingContext.request().getParam("id");
		if(null == id) {
			//todo dm handle for id not found
			//get single band
			routingContext.response()
					.setStatusCode(HttpResponseStatus.OK.code())
					.putHeader("content-type", "application/json; charset=utf-8")
					.end(Json.encodePrettily(bands.values()));

		} else {
			//get all bands
			routingContext.response()
					.setStatusCode(HttpResponseStatus.OK.code())
					.putHeader("content-type", "application/json; charset=utf-8")
					.end(Json.encodePrettily(bands.get(Integer.valueOf(id))));
		}
	}

	@Override
	public void createBand(RoutingContext routingContext) {
		Band band = Json.decodeValue(routingContext.getBodyAsString(), Band.class);

		bands.put(band.getId(), band);

		routingContext.response()
				.setStatusCode(HttpResponseStatus.CREATED.code())
				.putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(band));
	}

	@Override
	public void updateBand(RoutingContext routingContext) {
		Band band = Json.decodeValue(routingContext.getBodyAsString(), Band.class);
		String id = routingContext.request().getParam("id");

		if(null == id || null == bands.get(Integer.valueOf(id))) {
			//todo dm is this the right response code if requested domain object doesnt exist?
			routingContext.response().setStatusCode(400).end();
		} else {
			Band currentBand = bands.get(Integer.valueOf(id));
			currentBand.setName(band.getName());
			bands.put(Integer.valueOf(id), currentBand);
			routingContext.response()
					//todo dm is this the right response code for updated?
					.setStatusCode(HttpResponseStatus.OK.code())
					.putHeader("content-type", "application/json; charset=utf-8")
					.end(Json.encodePrettily(bands.get(Integer.valueOf(id))));
		}
	}

	@Override
	public void deleteBand(RoutingContext routingContext) {
		String id = routingContext.request().getParam("id");
		//todo dm this freezes up. why?

		if(null == id) {
			routingContext.response().setStatusCode(400).end();
		} else {
			bands.remove(Integer.valueOf(id));
			routingContext.response()
					.setStatusCode(HttpResponseStatus.NO_CONTENT.code());
		}
	}

	public static void seedBands(){
		Band moodyBlues = new Band("The Moody Blues");
		Band beatles = new Band("The Beatles");
		bands.put(moodyBlues.getId(), moodyBlues);
		bands.put(beatles.getId(), beatles);
	}

}
