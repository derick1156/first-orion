package com.firstorion.project.DerickMaloneVertxApp.service;

import com.firstorion.project.DerickMaloneVertxApp.domain.ThirdPartyBand;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

import java.util.HashMap;
import java.util.Map;

import static com.firstorion.project.DerickMaloneVertxApp.utilities.Constants.APPLICATION_JSON_UTF_8;
import static com.firstorion.project.DerickMaloneVertxApp.utilities.Constants.THIRD_PARTY_BAND_CREATED;
import static io.vertx.core.http.HttpHeaders.CONTENT_TYPE;

public class ThirdPartyServiceImpl implements ThirdPartyService {
	private static Map<Integer, ThirdPartyBand> thirdPartyBandsMap = new HashMap<>();
	private EventBus eventBus;

	public ThirdPartyServiceImpl(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	@Override
	public void getThirdPartyBands(RoutingContext routingContext) {
		routingContext.response()
				.setStatusCode(HttpResponseStatus.OK.code())
				.putHeader(CONTENT_TYPE, APPLICATION_JSON_UTF_8)
				.end(Json.encodePrettily(thirdPartyBandsMap.values()));

	}

	@Override
	public void createBandInThirdPartyRepository(RoutingContext routingContext) {
		ThirdPartyBand thirdPartyBand = Json.decodeValue(routingContext.getBodyAsString(), ThirdPartyBand.class);
		if(null != thirdPartyBand && null != thirdPartyBand.getBandName() && !doesBandAlreadyExist(thirdPartyBand)){
			//band did not already exist and a new one was created
			//todo dm may need to create a new band based on the name, so the atomic integer counter stays incremental and cant be overridden (would need to do this for bands too
			thirdPartyBandsMap.put(thirdPartyBand.getId(), thirdPartyBand);

			//publish the fact that a new band was created for whoever cares
			eventBus.publish(THIRD_PARTY_BAND_CREATED, thirdPartyBand.getBandName());

			routingContext.response()
					.setStatusCode(HttpResponseStatus.CREATED.code())
					.putHeader(CONTENT_TYPE, APPLICATION_JSON_UTF_8)
					.end(Json.encodePrettily(thirdPartyBand));
		} else {
			//band already existed and a new one was NOT created
			routingContext.response()
					.setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
					.putHeader(CONTENT_TYPE, APPLICATION_JSON_UTF_8)
					.end();
		}

	}

	private boolean doesBandAlreadyExist(ThirdPartyBand thirdPartyBand){
		for (Map.Entry<Integer, ThirdPartyBand> bandEntry : thirdPartyBandsMap.entrySet()) {
			if (bandEntry.getValue().getBandName().equalsIgnoreCase(thirdPartyBand.getBandName())) {
				return true;
			}
		}
		return false;
	}

	public static void seedBands(){
		ThirdPartyBand tomPetty = new ThirdPartyBand("Tom Petty and The Heartbreakers");
		ThirdPartyBand myMorningJacket = new ThirdPartyBand("My Morning Jacket");

		thirdPartyBandsMap.put(tomPetty.getId(),tomPetty);
		thirdPartyBandsMap.put(myMorningJacket.getId(),myMorningJacket);
	}
}
