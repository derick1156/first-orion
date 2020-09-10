package com.firstorion.project.DerickMaloneVertxApp.verticles;

import com.firstorion.project.DerickMaloneVertxApp.domain.Band;
import com.firstorion.project.DerickMaloneVertxApp.repository.BandsRepository;
import com.firstorion.project.DerickMaloneVertxApp.repository.BandsRepositoryImpl;
import com.firstorion.project.DerickMaloneVertxApp.service.BandsService;
import com.firstorion.project.DerickMaloneVertxApp.service.BandsServiceImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.atomic.AtomicInteger;

import static com.firstorion.project.DerickMaloneVertxApp.utilities.Constants.THIRD_PARTY_BAND_CREATED;
import static com.firstorion.project.DerickMaloneVertxApp.utilities.URL.BANDS;
import static com.firstorion.project.DerickMaloneVertxApp.utilities.URL.ID_PATH_PARAM;

public class MainBandsVerticle extends AbstractVerticle {

  //todos
  //make sure it is asynchronous (i believe the wat that the Vertx Start method and config works, it is async
  //get it deployed
  //clean up todos
    //clean up imports and commented code
    //clean up warnings
  //understand kotlin gradle.kts and why it was created that way
  //BONUS
  //logging
  //unit tests

      @Override
      public void start(Promise<Void> startPromise) throws Exception {
          HttpServer server = vertx.createHttpServer();

          RedissonClient redissonClient = configureRedissonClient();

          Router router = Router.router(vertx);

          BandsService bandsService = new BandsServiceImpl(new BandsRepositoryImpl(redissonClient));

          configureBandsRoute(router, bandsService);

          configureEventBus(bandsService);

          vertx.deployVerticle(ThirdPartyVerticle.class.getName());

          server.requestHandler(router).listen(8080);

      }

      private RedissonClient configureRedissonClient(){
          Config config = new Config();
          config.useSingleServer()
                  .setAddress("redis://redis-15903.c239.us-east-1-2.ec2.cloud.redislabs.com:15903")
                  //todo dm encrypt this

                  .setConnectionPoolSize(1)
                  .setConnectionMinimumIdleSize(1);

          return Redisson.create(config);
      }

      private void configureBandsRoute(Router router, BandsService bandsService){

          //reset the data when the Verticle is reinitialized just so we can start with a clean dataset. wouldnt be done like this in prod
          BandsRepositoryImpl.seedBandRepository();

          router.route(BANDS + "*").handler(BodyHandler.create());
          router.route().path(BANDS).method(HttpMethod.GET).handler(bandsService::getBands);
          router.route().path(BANDS + ID_PATH_PARAM).method(HttpMethod.GET).handler(bandsService::getBands);
          router.route().path(BANDS + ID_PATH_PARAM).method(HttpMethod.PUT).handler(bandsService::updateBand);
          router.route().path(BANDS).method(HttpMethod.POST).handler(bandsService::createBand);
          router.route().path(BANDS + ID_PATH_PARAM).method(HttpMethod.DELETE).handler(bandsService::deleteBand);

          router.errorHandler(500, rc -> {
              System.err.println("Handling failure");
              Throwable failure = rc.failure();
              if (failure != null) {
                  failure.printStackTrace();
              }
          });
      }

      private void configureEventBus(BandsService bandsService){
          EventBus eb = vertx.eventBus();

          eb.consumer(THIRD_PARTY_BAND_CREATED, message -> {
              System.out.println("A new Band was created in a Third Party System! Name: " + message.body().toString());
              if(bandsService.createBand(new Band(message.body().toString()))){
                  System.out.println("Third Party Band was propagated into our system! Name: " + message.body().toString());
              } else {
                  System.out.println("Third Party Band was NOT propagated into our system! Name: " + message.body().toString());
              }
          });
      }
}
