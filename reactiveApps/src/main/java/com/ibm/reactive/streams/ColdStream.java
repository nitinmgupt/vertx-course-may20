package com.ibm.reactive.streams;

import io.reactivex.Observable;

public class ColdStream {
    public static void main(String[] args) throws InterruptedException {
        // Source Stream: stream broadcast data to multiple subscribers
        Observable<Integer> coldStream = Observable.create(source-> {
           for(int i=0;i<10;i++) {
               source.onNext(i);
           }
           source.onComplete();
        });
        //subscriber1
        coldStream.subscribe(data->{
            System.out.println("David's Data is "+data);
        },System.out::println,()->System.out.println("done"));

        //subscriber2
        coldStream.subscribe(data->{
            System.out.println("Jame's Data is "+data);
        },System.out::println,()->System.out.println("done"));

        // late joining
        Thread.sleep(5000);
        //subscriber2
        coldStream.subscribe(data->{
            System.out.println("Karthik's Data is "+data);
        },System.out::println,()->System.out.println("done"));

    }
}
