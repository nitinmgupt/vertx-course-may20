package com.ibm.vertx.func;

import org.omg.Messaging.SyncScopeHelper;

//SAM : Single abstract method: interface having only one abstract method
interface Greeter {
    String greet();
}
//ways to implement/use this interface

//way -1 : declare class which implements Greeter
class GreeterImpl implements Greeter{
    @Override
    public String greet() {
        return "Hello";
    }
}
public class GreeterImplMain{
    public static void main(String[] args) {
        //create object for GreeterImpl
        Greeter g = new GreeterImpl();
        System.out.println(g.greet());
        //Way 2 : loosly coupled ; inner classes ; anonymous inner class
        Greeter greeter = new Greeter() {
            @Override
            public String greet() {
                return "Hello Vertx Fp";
            }
        };
        System.out.println(greeter.greet());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
            }
        };
        Thread thread = null;
        thread = new Thread(runnable);
        thread.start();

        //Way 3 : lambda style
        Greeter mygreeter = () -> {
                return "Hello Lambda Vertx Application";
        };
        System.out.println(mygreeter.greet());

        Runnable runner = ()->{
            System.out.println(" Lambda thread " +Thread.currentThread().getName());
        };
        thread = new Thread(runner);
        thread.start();


    }
}
