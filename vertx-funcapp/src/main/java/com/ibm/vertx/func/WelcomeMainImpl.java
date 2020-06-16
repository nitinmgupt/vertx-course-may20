package com.ibm.vertx.func;

//is it functional interface : no
@FunctionalInterface
interface Welcome {
    void sayHello();
}
public class WelcomeMainImpl {
    public static void main(String[] args) {
        Welcome hello = ()->{
           System.out.println("Welcome to Lambda Expression!");
        };
        //call sayHello
        hello.sayHello();
    }
}