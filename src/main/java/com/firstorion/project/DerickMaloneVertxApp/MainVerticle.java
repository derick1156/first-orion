package com.firstorion.project.DerickMaloneVertxApp;

import com.firstorion.project.DerickMaloneVertxApp.controller.BandsController;
import com.firstorion.project.DerickMaloneVertxApp.service.BandsServiceImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

import java.awt.image.BandedSampleModel;

import static com.firstorion.project.DerickMaloneVertxApp.utilities.URL.BANDS;
import static com.firstorion.project.DerickMaloneVertxApp.utilities.URL.ID_PATH_PARAM;

public class MainVerticle extends AbstractVerticle {

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

      Router router = Router.router(vertx);
      router.errorHandler(500, rc -> {
        System.err.println("Handling failure");
        Throwable failure = rc.failure();
        if (failure != null) {
          failure.printStackTrace();
        }
      });

      setupBandsRoute(router);

      server.requestHandler(router).listen(8080);

  }

  public void setupBandsRoute(Router router){

      BandsController bandsController = new BandsController(new BandsServiceImpl());
      //todo dm remove this when you have redis
      BandsServiceImpl.seedBands();
      router.route(BANDS + "*").handler(BodyHandler.create());
      router.route().path(BANDS).method(HttpMethod.GET).handler(bandsController::getBands);
      router.route().path(BANDS + ID_PATH_PARAM).method(HttpMethod.GET).handler(bandsController::getBands);
      router.route().path(BANDS + ID_PATH_PARAM).method(HttpMethod.PUT).handler(bandsController::updateBand);
      router.route().path(BANDS).method(HttpMethod.POST).handler(bandsController::createBand);
      router.route().path(BANDS + ID_PATH_PARAM).method(HttpMethod.DELETE).handler(bandsController::deleteBand);

  }


}
