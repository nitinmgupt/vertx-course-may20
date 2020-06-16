package com.ibm.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;

public class GreeterClientVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    //HTTP Clinet
    HttpClientOptions options = new HttpClientOptions();
    HttpClient client = vertx.createHttpClient(options);
    //to talk to server there are ways
    //way -1
//    client.getNow(3001, "localhost", "/", response -> {
//      System.out.println("Response info" + response.statusCode());
//      //handle response
//      response.bodyHandler(res -> {
//        System.out.println(res);
//      });
//    });

    //way -2
    client.request(HttpMethod.GET, 3001, "localhost", "/", response -> {
      System.out.println("Response info" + response.statusCode());
      //handle response
      response.bodyHandler(res -> {
        System.out.println(res);
      });
    }).end();

    //way -3
    client.get(3001, "localhost", "/", response -> {
      System.out.println("Response info" + response.statusCode());
      //handle response
      response.bodyHandler(res -> {
        System.out.println(res);
      });
    }).end();

  }
}
