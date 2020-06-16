package com.ibm.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;

public class HelloWorldWebServer extends AbstractVerticle {
  @Override
  public void start() throws Exception {
    //set server options/config
    HttpServerOptions config = new HttpServerOptions();
    config.setPort(4001);

//    HttpServer server = vertx.createHttpServer();
    HttpServer server = vertx.createHttpServer(config);
    //handling client requests
    server.requestHandler(context -> {
      //send a response
      HttpServerResponse response = context.response();
      response.end("<h1> Hello Vertx Server");
    });

    //start the http server
//    server.listen(3001,serverhandler->{
    server.listen(serverhandler->{
      if(serverhandler.succeeded()) {
        System.out.println("Server is UP:" + serverhandler.result().toString());
      } else {
        System.out.println("Server is FAILED" + serverhandler.cause().toString());
      }
    });
  }
}
