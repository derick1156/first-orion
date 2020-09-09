package com.firstorion.project.DerickMaloneVertxApp.service;

import com.firstorion.project.DerickMaloneVertxApp.domain.Band;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.firstorion.project.DerickMaloneVertxApp.utilities.Constants.BAND_KEY;

public class BandsServiceImpl implements BandsService {

	//todo dm remove
	static Map<Integer, Band> bands = new LinkedHashMap<>();

//	private final Vertx vertx;
//	private final RedisAPI redisAPI;
	private final RedissonClient redissonClient;

//	public BandsServiceImpl(Vertx vertx, RedisAPI redisAPI) {
	public BandsServiceImpl(RedissonClient redissonClient) {
		this.redissonClient = redissonClient;
//		this.vertx = vertx;
//		this.redisAPI = redisAPI;
	}

	@Override
	public void getBands(RoutingContext routingContext) {
		String id = routingContext.request().getParam("id");
		if(null == id) {
			//todo dm handle for id not found
			//get all bands
//			redisAPI.hgetall(BAND_KEY, res -> {
//				if(res.succeeded()){
//					System.out.println(res.result().get(0));
//				} else {
//					System.out.println("res failed");
//				}
//			});
			RMap bandsMap = redissonClient.getMap(BAND_KEY);
			routingContext.response()
					.setStatusCode(HttpResponseStatus.OK.code())
					.putHeader("content-type", "application/json; charset=utf-8")
					.end(Json.encodePrettily(bandsMap.readAllValues()));
//			routingContext.response()
//					.setStatusCode(HttpResponseStatus.OK.code())
//					.putHeader("content-type", "application/json; charset=utf-8")
//					.end(Json.encodePrettily(bands.values()));

		} else {
			//get single band
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

	public static void seedBands(RedissonClient redissonClient){
		Band moodyBlues = new Band("The Moody Blues");
		Band beatles = new Band("The Beatles");
		bands.put(moodyBlues.getId(), moodyBlues);
		bands.put(beatles.getId(), beatles);

		RMap map = redissonClient.getMap(BAND_KEY);

		map.put(moodyBlues.getId(), Json.encode(moodyBlues));
		map.put(beatles.getId(),Json.encode(beatles));
	}

}
