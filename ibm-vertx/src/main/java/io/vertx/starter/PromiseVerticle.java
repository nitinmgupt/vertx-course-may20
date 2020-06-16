package io.vertx.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;

//Promise is wrapper for Future

public class PromiseVerticle extends AbstractVerticle {

  private Future<String> getUser() {
    //Future<Void> future = Future.future();
    Promise<String> promise = Promise.promise();
    String userName = "Subramanian";
    if (userName != null) {
      promise.complete(userName);
    } else {
      promise.fail("No user found!");
    }
    return promise.future();
  }

  @Override
  public void start() throws Exception {
    //Future future = getUser().future();
    getUser().onComplete(h -> {
      if (h.succeeded()) {
        System.out.println(h.result());
      }
    });
  }
}
