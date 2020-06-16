package com.ibm.vertx;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class VertxInstanceDemo {

    public static void main(String[] args) {
        VertxOptions vertxOptions = new VertxOptions()
                .setWorkerPoolSize(100)
                .setHAEnabled(true)
                .setEventLoopPoolSize(5);
        Vertx vertx = Vertx.vertx(vertxOptions);
        System.out.println("VertxInstanceDemo.main");
    }
}