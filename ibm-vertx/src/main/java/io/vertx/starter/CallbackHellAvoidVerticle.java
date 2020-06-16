package io.vertx.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class CallbackHellAvoidVerticle extends AbstractVerticle {

  private Future<Void> prepareDatabse() {
    System.out.println("Data base is ready!");
    Future<Void> future = Future.future();
    future.complete();
    return future;
  }

  private Future<Void> startHttpServer() {
    System.out.println("Http Server!!...");
    Future<Void> future = Future.future();
    future.complete();
    return future;
  }

  private Future<String> sayHello(String world) {
    Future<String> future = Future.future();
    vertx.setTimer(100, h -> future.complete("Hello" + world));
    return future;
  }

  private Future<String> sayWorld() {
    Future<String> future = Future.future();
    vertx.setTimer(400, h -> future.complete("World"));
    return future;
  }


  @Override
  public void start() throws Exception {
    //traditional style : callback nesting /callhell
    prepareDatabse().onComplete(h -> {
      if (h.succeeded()) {
        //if prepare base success then call startHttpServer
        startHttpServer().onComplete(http -> {
          System.out.println("Server is Up!");
        });
      }

    });
    //future.compose method
    prepareDatabse().compose(h -> startHttpServer()).onComplete(http -> {
      if (http.succeeded()) {
        System.out.println("Server is Up inside compose");
      }
    });
    //
    sayWorld().onComplete(world -> {
      if (world.succeeded()) {
        sayHello(world.result()).onComplete(hello -> {
          System.out.println(hello.result());
        });
      }
    });
    //sayworld--output --sayHello(result)
    sayWorld().compose(m -> sayHello(m)).onComplete(h -> {
      if (h.succeeded()) {
        System.out.println(h.result());
      } else {
        System.out.println(h.cause());
      }
    });

    sayWorld().compose(m -> sayHello(m)).onComplete(res-> System.out.println(res.result()));
    //sayWorld().compose(m -> sayHello(m)).onComplete(res-> (res.succeeded()? System.out.println(res.result()) : System.out.println(res.cause()));
  }
}
