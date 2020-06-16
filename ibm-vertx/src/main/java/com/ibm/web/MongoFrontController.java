package com.ibm.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;


class BooksService {

  MongoClient mongoClient;

  BooksService() {
  }

  BooksService(MongoClient mongoClient) {
    this.mongoClient = mongoClient;
  }

  //api to get data from mongodb
  public Future<String> findAll() {
    Promise promise = Promise.promise();
    //async call to get docs
    mongoClient.find("books", new JsonObject(), lookup -> {
      if (lookup.failed()) {
        //send error
        promise.fail(lookup.cause());
        return;
      }
      //poupulate docments into jsonArray.
      JsonArray json = new JsonArray();
      for (JsonObject o : lookup.result()) {
        json.add(o);
      }
      promise.complete(json.encode());
    });
    return promise.future();
  }


}

//Application Controllers

class MongoBooksController extends AbstractVerticle {
  BooksService booksService;

  MongoBooksController() {
  }

  MongoBooksController(MongoClient mongoClient) {
    booksService = new BooksService(mongoClient);

  }

  public Router configBooksEndPoint() {
    Router bookrouter = Router.router(vertx);
    //end points
    bookrouter.get("/list").handler(ctx -> {
      booksService.findAll().onComplete(handler -> {
        if (handler.succeeded()) {
          //send response: JSON to client
          //set HTTP header
          ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
          //send response
          ctx.response().end(handler.result());

        }
      });
    });

    return bookrouter;
  }

}


//Main Controller
public class MongoFrontController extends AbstractVerticle {
  //server functionality
  HttpServer httpServer;
  HttpServerOptions options;
  MongoClient mongoClient;


  @Override
  public void start() throws Exception {
    options = new HttpServerOptions();
    options.setPort(3000);
    options.setHost("localhost");
    httpServer = vertx.createHttpServer(options);

    //Initalize mongoConnection
    mongoClient = MongoClient.createShared(vertx, new JsonObject().put("db_name", "mybooks"));
    //Create Application Controller Object
    MongoBooksController booksCtrl = new MongoBooksController(mongoClient);

    //Main router Object
    Router appRouter = Router.router(vertx);

    //Main url where web server handles request and redirect to other controllers
    appRouter.mountSubRouter("/api/books", booksCtrl.configBooksEndPoint());

    //requestHandler is redirecting to router for addressing multiple endpoints
    httpServer.requestHandler(appRouter);


    //start the http server
    httpServer.listen(server -> {
      if (server.succeeded()) {
        System.out.println("Server is Up!");
      }
    });
  }
}
