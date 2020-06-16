package com.ibm.vertx.microservice;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;


public class Receiver extends AbstractVerticle {
  @Override
  public void start() throws Exception {
    System.out.println("Consumer is  Ready!");

    EventBus eb = vertx.eventBus();
    eb.consumer("news.uk.sport", message -> {
      System.out.println("I have received a message: " + message.body());
    });
  }
}
