package com.ibm.reactive.streams;

import io.reactivex.Observable;

public class SequenceStream {
    public static void main(String[] args) {
        //create stream from scratch
        Observable<Integer> stream = Observable.create(source -> {
            //sequence of nos from 1 to 10
            for (int i = 1; i <= 10; i++) {
                source.onNext(i);
            }
            source.onComplete();
        });

        stream.subscribe(System.out::println, System.out::println, () -> System.out.println("done"));

    }
}
