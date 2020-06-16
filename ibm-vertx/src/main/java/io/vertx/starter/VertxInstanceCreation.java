package io.vertx.starter;

import io.vertx.core.Vertx;

public class VertxInstanceCreation {
  public static void main(String[] args) {
    //Create Verx instance
    Vertx  myvertx = Vertx.vertx();
    System.out.println(myvertx.toString());

  }
}
