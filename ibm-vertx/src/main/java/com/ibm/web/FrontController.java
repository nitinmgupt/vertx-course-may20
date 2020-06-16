package com.ibm.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;

//Application Controllers

class BooksController extends AbstractVerticle {

  public Router configBooksEndPoint() {
    Router bookrouter = Router.router(vertx);
    //end points
    bookrouter.get("/list").handler(ctx -> {
      ctx.response().end("books List");
    });

    return bookrouter;
  }

}


//Main Controller
public class FrontController extends AbstractVerticle {
  //server functionality
  HttpServer httpServer;
  HttpServerOptions options;


  @Override
  public void start() throws Exception {
    options = new HttpServerOptions();
    options.setPort(3000);
    options.setHost("localhost");
    httpServer = vertx.createHttpServer(options);

    //Create Application Controller Object
    BooksController booksCtrl = new BooksController();

    //Main router Object
    Router appRouter = Router.router(vertx);

    //Main url where web server handles request and redirect to other controllers
    appRouter.mountSubRouter("/api/books", booksCtrl.configBooksEndPoint());

    //requestHandler
    httpServer.requestHandler(appRouter);


    //start the server
    httpServer.listen(server -> {
      if (server.succeeded()) {
        System.out.println("Server is Up!");
      }
    });
  }
}
