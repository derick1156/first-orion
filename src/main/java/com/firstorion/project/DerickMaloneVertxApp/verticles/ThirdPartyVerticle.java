package com.firstorion.project.DerickMaloneVertxApp.verticles;

import com.firstorion.project.DerickMaloneVertxApp.service.ThirdPartyService;
import com.firstorion.project.DerickMaloneVertxApp.service.ThirdPartyServiceImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

import static com.firstorion.project.DerickMaloneVertxApp.utilities.URL.THIRD_PARTY;

public class ThirdPartyVerticle extends AbstractVerticle {

    //some hypothetical third party that may exist that can also manage a catalog of bands. simply exists to demo the event bus
      @Override
      public void start(Promise<Void> startPromise) throws Exception {
          HttpServer server = vertx.createHttpServer();

          Router router = Router.router(vertx);

          configureThirdPartyRouter(router);

          server.requestHandler(router).listen(8090);

      }

      public void configureThirdPartyRouter(Router router){

          ThirdPartyServiceImpl.seedBands();
          ThirdPartyService thirdPartyService = new ThirdPartyServiceImpl(vertx.eventBus());
          router.route(THIRD_PARTY + "*").handler(BodyHandler.create());
          router.route().path(THIRD_PARTY).method(HttpMethod.GET).handler(thirdPartyService::getThirdPartyBands);
          router.route().path(THIRD_PARTY).method(HttpMethod.POST).handler(thirdPartyService::createBandInThirdPartyRepository);

          router.errorHandler(500, rc -> {
              System.err.println("Handling failure");
              Throwable failure = rc.failure();
              if (failure != null) {
                  failure.printStackTrace();
              }
          });
      }

}
