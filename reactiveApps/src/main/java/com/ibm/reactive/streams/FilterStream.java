package com.ibm.reactive.streams;

import io.reactivex.Observable;

public class FilterStream {
    public static void main(String[] args) {
        Observable<Integer> upStream = Observable.create(source -> {
            //sequence of nos from 1 to 10
            for (int i = 1; i <= 10; i++) {
                source.onNext(i);
            }
            source.onComplete();
        });
        upStream.filter(item -> item % 2==0)
                .map(item->item*10)
                .subscribe(System.out::println, System.out::println, () -> System.out.println("done"));

    }
}
