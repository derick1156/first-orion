package com.firstorion.project.DerickMaloneVertxApp.service;

import com.firstorion.project.DerickMaloneVertxApp.domain.Band;
import com.firstorion.project.DerickMaloneVertxApp.repository.BandsRepository;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

import static com.firstorion.project.DerickMaloneVertxApp.utilities.Constants.APPLICATION_JSON_UTF_8;
import static io.vertx.core.http.HttpHeaders.CONTENT_TYPE;

public class BandsServiceImpl implements BandsService {
	private BandsRepository bandsRepository;

	public BandsServiceImpl(BandsRepository bandsRepository) {
		this.bandsRepository = bandsRepository;
	}

	@Override
	public void getBands(RoutingContext routingContext) {
		String id = routingContext.request().getParam("id");

		if(null == id) {
			//get all bands
			routingContext.response()
					.setStatusCode(HttpResponseStatus.OK.code())
					.putHeader(CONTENT_TYPE, APPLICATION_JSON_UTF_8)
					.end(Json.encodePrettily(bandsRepository.getAllBands()));
		} else {
			//get single band
			Band band = bandsRepository.getBandById(Integer.valueOf(id));
			if(null != band) {
				routingContext.response()
						.setStatusCode(HttpResponseStatus.OK.code())
						.putHeader(CONTENT_TYPE, APPLICATION_JSON_UTF_8)
						.end(Json.encodePrettily(band));
			} else {
				//requested ID not found
				routingContext.response()
						.setStatusCode(HttpResponseStatus.NOT_FOUND.code())
						.end();
			}
		}
	}

	@Override
	public void createBand(RoutingContext routingContext) {
		Band band = Json.decodeValue(routingContext.getBodyAsString(), Band.class);

		if(createBand(band)) {
			//band did not already exist and a new one was created
			routingContext.response()
					.setStatusCode(HttpResponseStatus.CREATED.code())
					.putHeader(CONTENT_TYPE, APPLICATION_JSON_UTF_8)
					.end(Json.encodePrettily(band));
		} else {
			//band already existed and a new one was NOT created
			routingContext.response()
					.setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
					.end();
		}
	}

	@Override
	public boolean createBand(Band band){
		if(isValidBand(band) && !doesBandAlreadyExist(band) && null == bandsRepository.insertBand(band)) {
			return true;
		}
		return false;
	}

	@Override
	public void updateBand(RoutingContext routingContext) {
		String id = routingContext.request().getParam("id");
		Band currentBand = null != id ? bandsRepository.getBandById(Integer.valueOf(id)) : null;

		if(null == currentBand) {
			//no id was given, or the id given doesnt exist
			routingContext.response()
					.setStatusCode(HttpResponseStatus.NOT_FOUND.code())
					.end();
		} else {
			Band requestBand = Json.decodeValue(routingContext.getBodyAsString(), Band.class);

			if(!isValidBand(requestBand) || doesBandAlreadyExist(requestBand)) {
				//band in request was invalid
				routingContext.response()
						.setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
						.end();
			} else {
				currentBand.setName(requestBand.getName());
				bandsRepository.updateBand(currentBand);

				routingContext.response()
						.setStatusCode(HttpResponseStatus.OK.code())
						.putHeader(CONTENT_TYPE, APPLICATION_JSON_UTF_8)
						.end(Json.encodePrettily(currentBand));
			}
		}
	}

	@Override
	public void deleteBand(RoutingContext routingContext) {
		String id = routingContext.request().getParam("id");
		Band currentBand = null != id ? bandsRepository.getBandById(Integer.valueOf(id)) : null;

		if(null == currentBand) {
			//no id was given, or the id given doesnt exist
			routingContext.response().setStatusCode(HttpResponseStatus.NOT_FOUND.code()).end();
		} else {
			bandsRepository.deleteBand(currentBand.getId());
			routingContext.response()
					.setStatusCode(HttpResponseStatus.NO_CONTENT.code())
					.end();
		}
	}

	private boolean doesBandAlreadyExist(Band band){
		for (Band existingBand: bandsRepository.getAllBands()) {
			if (existingBand.getName().equalsIgnoreCase(band.getName())) {
				return true;
			}
		}
		return false;
	}

	private boolean isValidBand(Band band){
		 return (null != band && null != band.getName() && !band.getName().isEmpty());
	}

}
