package com.ibm.reactive.streams;

import io.reactivex.Observable;

public class BackPressureDemo {
    public static void main(String[] args) {
        //take is back pressure operator
        Observable<Integer> stream = Observable.range(1, 200000).take(20).filter(i -> i % 2 == 0);
        stream.subscribe(System.out::println);
    }
}
