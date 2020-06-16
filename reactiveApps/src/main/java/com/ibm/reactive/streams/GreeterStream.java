package com.ibm.reactive.streams;

import io.reactivex.Observable;

public class GreeterStream {
    public static void main(String[] args) {
        // create stream from scratch
        Observable<String> stream = Observable.create(source->{
           source.onNext("nitin");
           source.onNext("Ram");
           //push error
           source.onError(new Exception("Something went wrong")); //emit error event
           source.onComplete(); // emit complete signal and stream is closed. nothing will be pushed further.
           source.onNext("kartik");
        });

        //subscriber: three signal - data, error, complete
        // way-1
        //stream.subscribe(data->System.out.println(data),err->System.out.println(err),()->System.out.println("done"));
        // way-2
        stream.subscribe(System.out::println,System.out::println,()->System.out.println("done"));
        //stream.doOnComplete()

    }
}
