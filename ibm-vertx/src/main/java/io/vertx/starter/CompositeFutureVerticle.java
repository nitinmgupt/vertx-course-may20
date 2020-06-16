package io.vertx.starter;
  import io.vertx.core.AbstractVerticle;
  import io.vertx.core.CompositeFuture;
  import io.vertx.core.Future;
  import io.vertx.core.Promise;

  import java.util.Arrays;
  import java.util.List;

//Promise is wrapper for Future
public class CompositeFutureVerticle extends AbstractVerticle {

  private Future<String> getName1() {
    //Future<Void> future = Future.future();
    Promise<String> promise = Promise.promise();
    promise.complete("Subramanian");
    return promise.future();
  }

  private Future<String> getName2() {
    //Future<Void> future = Future.future();
    Promise<String> promise = Promise.promise();
    promise.complete("Ram");

    return promise.future();
  }

  private Future<String> getName3() {
    //Future<Void> future = Future.future();
    Promise<String> promise = Promise.promise();
    promise.complete("Karthik");
    return promise.future();
  }


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
    //how to grab all promise/future results into one single Result
    List<Future> futureList = Arrays.asList(getName1(), getName2(), getName3());
    //
    CompositeFuture.all(futureList).onComplete(handler -> {
      handler.result().list().forEach(System.out::println);
    });
  }
}
