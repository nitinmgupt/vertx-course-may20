package com.ibm.web;

import io.vertx.core.AbstractVerticle;

public class ApplicationVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    //How to deploy verticle
    vertx.deployVerticle(new GreeterServerVerticle());
    vertx.deployVerticle(new GreeterClientVerticle());
  }
}
