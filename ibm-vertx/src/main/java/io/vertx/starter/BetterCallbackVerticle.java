package io.vertx.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

public class BetterCallbackVerticle extends AbstractVerticle {
  public void getUser(Handler<AsyncResult<String>> aHandler) {
    String fakeuser = "Nitin";
    if (fakeuser!=null) {
      //handle success
      aHandler.handle(Future.succeededFuture(fakeuser));
    } else {
      aHandler.handle(Future.failedFuture("No User Found!!"));

    }
  }

  public void login(String username, Handler<AsyncResult<String>> aHandler) {
    String fakeuser = "Nitin";
    if ("Nitin".equalsIgnoreCase(username)) {
      //handle success
      aHandler.handle(Future.succeededFuture("Login Success!!"));
    } else {
      aHandler.handle(Future.failedFuture("Login Failed"));

    }
  }

  @Override
  public void start() throws Exception {
    getUser(ar->{
      if(ar.succeeded()){
        System.out.println(ar.result());
        login(ar.result(),lar-> {
          System.out.println(lar.succeeded()?lar.result():lar.cause());
        });
      }
    });
  }

  @Override
  public void stop() throws Exception {
    super.stop();
  }
}
