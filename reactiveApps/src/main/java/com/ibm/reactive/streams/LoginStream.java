package com.ibm.reactive.streams;

import io.reactivex.Observable;

public class LoginStream {
    public static void main(String[] args) {
        // create stream from scratch
        Observable<String> stream = Observable.create(source->{
            String username = "admin";
            String password = "admin";
            if("admin".equalsIgnoreCase(username) && "admin".equalsIgnoreCase(password)) {
                //emit success message
                source.onNext("Login Success");
                source.onComplete();
            } else {
                source.onError(new Exception("Login Failed")); //emit error event
            }
        });

        //subscriber: three signal - data, error, complete
        // way-1
        //stream.subscribe(data->System.out.println(data),err->System.out.println(err),()->System.out.println("done"));
        // way-2
        stream.subscribe(System.out::println,System.out::println,()->System.out.println("done"));
    }
}
