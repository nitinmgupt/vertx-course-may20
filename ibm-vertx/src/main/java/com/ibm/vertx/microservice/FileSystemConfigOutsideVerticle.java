package com.ibm.vertx.microservice;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerOptions;

public class FileSystemConfigOutsideVerticle extends AbstractVerticle {
  @Override
  public void start() throws Exception {
    super.start();
    //vertx.deployVerticle(new FileSystemConfigVerticle());
    System.out.println(config().toString());
    System.out.println(config().getString("name"));
    System.out.println(config().getInteger("port"));

    vertx.createHttpServer(new HttpServerOptions().setPort(config().getInteger("port")))
      .requestHandler(context -> {
        context.response().putHeader(HttpHeaders.CONTENT_TYPE, "text/html");
        context.response().end("==> Server Details: <br>Name: " + config().getString("name") + "<br>Version: " + config().getInteger("port"));
      })
      .listen(serverHandler -> {
        if (serverHandler.succeeded()) {
          System.out.println("Server is Up " + serverHandler.result());
        } else {
          System.out.println(serverHandler.cause());
        }
      });

    // try to reformat this using compose()
  }
}
