package com.ibm.reactive.streams;

import io.reactivex.Maybe;
import io.reactivex.Single;

public class SingleDemo {
    public static void main(String[] args) {
        //emit only data
        Single.just("Hello").subscribe(System.out::println);
        Single single=Single.create(source -> {
            try {
                if ("hello".equals("helloxxx")) {
                    source.onSuccess("Hello Single");
                    source.onSuccess("How are you");
                    source.onSuccess("Hai");
                    source.onSuccess("welcome");



                } else {
                    throw new RuntimeException("some thing went wrong!");
                }
            } catch (RuntimeException e) {
                source.onError(e);

            }

        });
        single.subscribe(System.out::println, System.out::println);

        //Maybe.Just : Single
        Maybe.just("may be").subscribe(System.out::println, System.out::println);
        //Maybe.Empty : Only completed Single
        Maybe.empty().subscribe(System.out::println, System.out::println,()->System.out.println("done"));
        //Maybe.error : Only error
        Maybe.error(new Exception("Error")).subscribe(System.out::println);

        //Only Complete
        //Only Error
    }
}
