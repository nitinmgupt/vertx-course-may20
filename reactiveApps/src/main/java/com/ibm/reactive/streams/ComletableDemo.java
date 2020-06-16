package com.ibm.reactive.streams;

import io.reactivex.Completable;

import java.util.concurrent.TimeUnit;

public class ComletableDemo {
    public static void main(String[] args) throws InterruptedException {
        Completable.complete().subscribe(()-> System.out.println("done"));
        Thread.sleep(300);

        Completable.complete().delay(1000, TimeUnit.MICROSECONDS).subscribe(()->System.out.println("Time Out"));
        Thread.sleep(5000);
    }
}
