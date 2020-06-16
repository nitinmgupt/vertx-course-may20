package com.ibm.vertx.microservice;

import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.core.http.HttpServerResponse;

public class ReactiveHTTPServer extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    super.start();

    //Reactive and non-blocking http server
    // no need to pass handler
    HttpServer server = vertx.createHttpServer();
    //handling request
    // stream is converted into backpressure stream.
      server.requestStream().toFlowable().subscribe(req -> {
        HttpServerResponse resp = req.response();
        String contentType = req.getHeader("Content-Type");
        if (contentType != null) {
          resp.putHeader("Content-Type", contentType);
        }
        resp.setChunked(true);
        req.toFlowable().subscribe(
          resp::write,
          err -> {},
          resp::end
        );
      });
      server.listen(8085);
  }
}
