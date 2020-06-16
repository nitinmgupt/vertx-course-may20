package com.ibm.reactive.streams;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

public class FlowableBackPressure {
    public static void main(String[] args) {
        Flowable<Integer> flowStream = Flowable.create(source->{
            for(int i=0;i<2000;i++){
                source.onNext(i);
            }
        }, BackpressureStrategy.BUFFER);
        //Buffers all values until downstream consumes it
        flowStream.subscribe(System.out::println);
    }
}
