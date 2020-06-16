package com.ibm.vertx.microservice;

import com.ibm.web.GreeterClientVerticle;
import com.ibm.web.GreeterServerVerticle;
import io.vertx.core.AbstractVerticle;

public class DeployerVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    //How to deploy verticle
    vertx.deployVerticle(new Sender());
    vertx.deployVerticle(new Receiver());
  }
}
