package io.vertx.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class NestedCallbackVerticle extends AbstractVerticle {

  //getUser : which is returning user information
  private Future<String> getUser() {
    Future future = Future.future();
    //biz logic
    String fakeUser = "Subramanian";
    if (fakeUser != null) {
      future.complete(fakeUser);
    } else {
      future.fail("User Not Found!!");
    }
    return future;
  }

  private Future<String> login(String userName) {
    Future future = Future.future();
    //biz logic
    if (userName.equals("Subramanian")) {
      future.complete("Login Success");
    } else {
      future.fail("Login Failed!");
    }
    return future;
  }

  private Future<String> showPage(String status) {
    Future future = Future.future();
    //biz logic
    if (status.equals("Login Success")) {
      future.complete("Welcome to Premium Page");
    } else {
      future.fail("Welcome to Guest Page");
    }
    return future;
  }


  @Override
  public void start() throws Exception {
    //nested callback
    getUser().onComplete(h -> {
      if (h.succeeded()) {
        //you get the user here ; if you get continue with login
        System.out.println("User " + h.result());
        login(h.result()).onComplete(loginHandler -> {
          if (loginHandler.succeeded()) {
            System.out.println(loginHandler.result());
            showPage(loginHandler.result()).onComplete(pageHandler -> {
              if (pageHandler.succeeded()) {
                System.out.println(pageHandler.result());
              } else {
                System.out.println(pageHandler.cause());
              }
            });
          } else {
            System.out.println(loginHandler.cause());
          }
        });

      } else {
        //you get error : if you get error stop the flow
        System.out.println(h.cause());
      }

    });
  }
}
