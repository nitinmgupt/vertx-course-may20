package io.vertx.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

import javax.xml.ws.AsyncHandler;

public class TimerAsyncVerticle extends AbstractVerticle {
  private Future<String> delay (long delay, String message) {
    Future  future = Future.future();
    vertx.setTimer(delay,cb-> {
      //wrap async result into Future
      //System.out.println("Timer is ready....");
      future.complete("Timer is ready....");
    });
    return future;
  }

  private void syncApi(String message) {
    System.out.println(message);
  }

  @Override
  public void start() throws Exception {
    //sync call
    syncApi("started");
    //async
    //called need to grab async result
    //delay(1000,"hello");
    delay(5000,"hello").onComplete(handler-> {
      System.out.println(handler.succeeded()?handler.result():handler.cause().getMessage());
    });
    //sync call
    syncApi("end");
  }
}
