package com.ibm.io;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

public class BlockingVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {

    //blocking
    vertx.<String>executeBlocking(blockingHanlder -> {
      //blocking code will inside this callback function
      try {
        System.out.println("Waiting in Blocking mode!!");
        Thread.sleep(8000);
      } catch (InterruptedException es) {
        System.out.println(es);
      }
      //The result of blocking api
      String result = "Sleep is done, hand over me to some one!";
      //Wrap the result in side future
      blockingHanlder.complete(result);

    }, resultHandler -> {
      //you can peform non blocking operations
      System.out.println("Blocking operation is completed");
      System.out.println(resultHandler.result());
    });


  }
}
