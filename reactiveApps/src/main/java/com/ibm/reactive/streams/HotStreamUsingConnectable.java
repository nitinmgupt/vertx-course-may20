package com.ibm.reactive.streams;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observables.ConnectableObservable;

import java.util.concurrent.TimeUnit;

public class HotStreamUsingConnectable {

    public static void main(String[] args) throws InterruptedException {

        // create Hold Stream
        //this stream starts publishing long numeber every 1 sec
        Observable<Long> coldstream = Observable.interval(1, TimeUnit.SECONDS);

        //we are converting cold to hot using publish.refCount
        //Hot Stream
        Observable<Long> connectableObservable = coldstream.publish().refCount();

        System.out.println("Streaming begins");

        Disposable subu = connectableObservable.doOnSubscribe(data -> System.out.println("Subu joined"))
                .doFinally(() -> System.out.println("Subramanian Left"))
                .subscribe(item -> System.out.println("Subu got item" + item));

        Thread.sleep(2000);

        Disposable ram = connectableObservable.doOnSubscribe(data -> System.out.println("Ram joined"))
                .doFinally(() -> System.out.println("Ram Left"))
                .subscribe(item -> System.out.println("Ram got item" + item));

        Thread.sleep(3000);
        //moving out /unscribing from the room
        subu.dispose();

        Thread.sleep(4000);



    }
}
