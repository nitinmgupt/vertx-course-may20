package io.vertx.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

public class BasicFutureVerticle extends AbstractVerticle {

  //methods which return future
  private Future<Void> getEmptyFuture() {
    //Create Future Object
    Future future = Future.future();
    //Invoke async completion event
    future.complete(); // This trigger succeed event.
    return future;

  }

  //return the basic Future with payload(data)
  private Future<String> getDataFuture() {
    //Create Future Object
    Future future = Future.future();
    //Invoke async completion event
    future.complete("Hello I am Async Result");
    return future;
  }

  private Future<String> getError() {
    Future future = Future.future();
    //Invoke async completion event
    future.fail("Something went wrong!!!");
    return future;
  }

  // send sucess/failure based on biz use case
  private Future<String> validate(String username, String password) {
    Future<String> future = Future.future();
    //biz logic
    if("admin".equalsIgnoreCase(username) && "admin".equalsIgnoreCase(password)) {
      future.complete("Login Success");
    } else {
      future.fail("Login Failed");
    }
    return future;
  }

  @Override
  public void start() throws Exception {
    System.out.println("Future Starts");
    //Declare Future reference to handle Future
    Future future = null;
    future = getEmptyFuture();

    //future returns empty result
    if (future.succeeded()) {
      System.out.println("Future return success message");
    } else {
      System.out.println("Future not returned any thing!");
    }
    //get string data
    future = getDataFuture();
    //old java style
    future.setHandler(new Handler<AsyncResult>() {
      @Override
      public void handle(AsyncResult event) {
        if (event.succeeded()) {
          System.out.println(event.result());
        }

      }
    });
    //lambda style
    //getDataFuture().setHandler(h -> {
    getDataFuture().onComplete(h -> {
      if (h.succeeded()) {
        System.out.println(h.result());
      }
    });

    //replacer for setHanlder :this only for your api is returning success only
    getDataFuture().onSuccess(result -> System.out.println("Lambda: "+result));
    getDataFuture().onSuccess(System.out::println);

    //getError().setHandler(handler->{
    getError().onComplete(handler->{
      if (handler.failed()) {
        // grab the message
        System.out.println("Lambda: "+handler.cause());
      }
    });

    getError().onFailure(System.out::println);

    // call the validate method
    validate("admin","admin").setHandler(handler->{
      if(handler.succeeded())
        System.out.println("old style:"+handler.result());
      else
        System.out.println(handler.cause());
    });

    validate("admin","admin").onSuccess(System.out::println);
    validate("admin","amd").onFailure(System.out::println);
  }
}


