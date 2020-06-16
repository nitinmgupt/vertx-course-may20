package com.ibm.reactive.streams;

import io.reactivex.Observable;

public class StreamTransformationUsingMap {
    public static void main(String[] args) {
        //source stream
        //create stream from scratch
//        Observable<Integer> sourceStream = Observable.create(source -> {
//            //sequence of nos from 1 to 10
//            for (int i = 1; i <= 10; i++) {
//                source.onNext(i);
//            }
//            source.onComplete();
//        });
//
//        //apply transformation ; multiply the item by 2
////        Observable<Integer> downStream= sourceStream.map(item->{
////            return item * 2;  // transformed stream
////        });
//        Observable<Integer> downStream = sourceStream.map(item -> item * 2);
//
//        downStream.subscribe(System.out::println, System.out::println, () -> System.out.println("done"));

        Observable<Integer> upStream = Observable.create(source -> {
            //sequence of nos from 1 to 10
            for (int i = 1; i <= 10; i++) {
                source.onNext(i);
            }
            source.onComplete();
        });
        upStream.map(item -> item * 2).subscribe(System.out::println, System.out::println, () -> System.out.println("done"));

    }
}
