package com.ibm.reactive.streams;

import io.reactivex.Observable;
import io.reactivex.observables.ConnectableObservable;

public class HotStreamDelay {

    public static void main(String[] args) throws InterruptedException {

        // create Hold Stream
        Observable<Integer> coldstream = Observable.create(subscriber -> {
            for (int i = 0; i < 500; i++) {
                Thread.sleep(1000);
                subscriber.onNext(i);
            }
        }); // Create Connectable Observable : Convert cold to Hot
        ConnectableObservable connectableObservable = coldstream.publish();

        System.out.println("Streaming begins");
        connectableObservable.subscribe(data -> {
            System.out.println("Subramanian : " + data);
        });
        connectableObservable.subscribe(data -> {
            System.out.println("Ram :  " + data);
        });

        Thread.sleep(200);

        connectableObservable.subscribe(data -> {
            System.out.println("................");
            System.out.println("John :  " + data);
        });

        connectableObservable.connect();


    }
}
