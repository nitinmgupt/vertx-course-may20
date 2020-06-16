package com.ibm.reactive.streams;

import io.reactivex.Observable;
import io.reactivex.subjects.AsyncSubject;

public class AsyncSubjectDemo {
    public static void main(String[] args) {
        //One to Many : multi cast Streaming using Subject
        //Async Subject requires source Stream
        // Source Stream
//        Observable<Integer> sourceStream = Observable.create(subscriber -> {
//            for (int i = 0; i <= 20; i++) {
//                subscriber.onNext(i);
//            }
//            subscriber.onComplete();
//        });
        //cold stream
        Observable<Integer> sourceStream = Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        //Create Async Subject
        AsyncSubject<Integer> asyncSubject = AsyncSubject.create();

        //connect source stream with async subject
        sourceStream.subscribe(asyncSubject);

        asyncSubject.subscribe(data -> {
            System.out.println("Subu's AsyncSubject Subject :" + data);
        }, err -> {
            System.out.println(err);
        }, () -> {
            System.out.println("Done!");
        });
        asyncSubject.subscribe(data -> {
            System.out.println("Ram's AsyncSubject Subject :" + data);
        }, err -> {
            System.out.println(err);
        }, () -> {
            System.out.println("Done!");
        });

    }
}
