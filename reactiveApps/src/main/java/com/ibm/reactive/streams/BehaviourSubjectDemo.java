package com.ibm.reactive.streams;


import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class BehaviourSubjectDemo {
    private static void addSomeDelay() {
        try {
            System.out.println("Wait for some seconds");
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        //soure stream
        Observable<Long> coldb = Observable.interval(1000, TimeUnit.MILLISECONDS);

        //create Behaviour Subject
        BehaviorSubject<Long> behaviorSubject = BehaviorSubject.create();

        coldb.subscribe(behaviorSubject);

        addSomeDelay();
        // BehaviourSubject emits the most recently item at the time of subscription
        behaviorSubject.subscribe(data -> {
            System.out.println("Subu's BehaviorSubject Subject :" + data);
        }, err -> {
            System.out.println(err);
        }, () -> {
            System.out.println("Done!");
        });

        behaviorSubject.subscribe(data -> {
            System.out.println("Ram's BehaviorSubject Subject :" + data);
        }, err -> {
            System.out.println(err);
        }, () -> {
            System.out.println("Done!");
        });

        addSomeDelay();

    }
}
