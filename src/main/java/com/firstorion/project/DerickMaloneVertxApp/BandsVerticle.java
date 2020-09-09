package com.firstorion.project.DerickMaloneVertxApp;

import com.firstorion.project.DerickMaloneVertxApp.service.BandsService;
import com.firstorion.project.DerickMaloneVertxApp.service.BandsServiceImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import static com.firstorion.project.DerickMaloneVertxApp.utilities.URL.BANDS;
import static com.firstorion.project.DerickMaloneVertxApp.utilities.URL.ID_PATH_PARAM;

public class BandsVerticle extends AbstractVerticle {

  //todos
  //add redis
  //get/post/put/delete for bands
    //all that for the other domain objects
  //http status codes
  //get it in git
  //get it deployed
  //clean up todos
  //understand kotlin gradle.kts and why it was created that way
  //BONUS
  //event bus
  //logging
  //unit tests


      @Override
      public void start(Promise<Void> startPromise) throws Exception {
          HttpServer server = vertx.createHttpServer();

          Config config = new Config();
          config.useSingleServer()
                  .setAddress("redis://redis-18311.c239.us-east-1-2.ec2.cloud.redislabs.com:18311")

                  .setConnectionPoolSize(1)
          .setConnectionMinimumIdleSize(1);

          RedissonClient redissonClient = Redisson.create(config);

          Router router = Router.router(vertx);
          router.errorHandler(500, rc -> {
            System.err.println("Handling failure");
            Throwable failure = rc.failure();
            if (failure != null) {
              failure.printStackTrace();
            }
          });

          setupBandsRoute(router, redissonClient);

          server.requestHandler(router).listen(8080);

      }

      public void setupBandsRoute(Router router, RedissonClient redissonClient){

          //todo dm remove this when you have redis
          BandsServiceImpl.seedBands(redissonClient);
          BandsService bandsService = new BandsServiceImpl(redissonClient);
          router.route(BANDS + "*").handler(BodyHandler.create());
          router.route().path(BANDS).method(HttpMethod.GET).handler(bandsService::getBands);
          router.route().path(BANDS + ID_PATH_PARAM).method(HttpMethod.GET).handler(bandsService::getBands);
          router.route().path(BANDS + ID_PATH_PARAM).method(HttpMethod.PUT).handler(bandsService::updateBand);
          router.route().path(BANDS).method(HttpMethod.POST).handler(bandsService::createBand);
          router.route().path(BANDS + ID_PATH_PARAM).method(HttpMethod.DELETE).handler(bandsService::deleteBand);

      }


}
