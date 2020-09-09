package com.firstorion.project.DerickMaloneVertxApp.controller;

import com.firstorion.project.DerickMaloneVertxApp.service.BandsService;
import io.vertx.ext.web.RoutingContext;

public class BandsController {

    BandsService bandsService;

    public BandsController(BandsService bandsService) {
        this.bandsService = bandsService;
    }

    public void getBands(RoutingContext routingContext) {
        bandsService.getBands(routingContext);
    }

    public void createBand(RoutingContext routingContext) {
        bandsService.createBand(routingContext);
    }

    public void updateBand(RoutingContext routingContext) {
        bandsService.updateBand(routingContext);
    }

    public void deleteBand(RoutingContext routingContext) {
        bandsService.deleteBand(routingContext);
    }

}
