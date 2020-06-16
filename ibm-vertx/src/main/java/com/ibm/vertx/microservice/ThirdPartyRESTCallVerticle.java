package com.ibm.vertx.microservice;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

public class ThirdPartyRESTCallVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    super.start();
    //Create WebClient instance
    WebClient client = WebClient.create(vertx);

    //apis to call rest end point
    //host,resource(url)
    client.get("jsonplaceholder.typicode.com", "/users").send(ar -> {
      if (ar.succeeded()) {
        HttpResponse<Buffer> response = ar.result();
        //process result
        System.out.println(response.bodyAsJsonArray().encodePrettily());
      } else {
        System.out.println(ar.cause());
      }
    });
  }
}
