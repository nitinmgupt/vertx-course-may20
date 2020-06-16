package com.ibm.vertx.func;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

class LoopPrinter {
    public static void print(String name) {
        System.out.println("=="+name);
    }
}

public class BuiltInFunctionalInterfaces {


    public static void main(String[] args) {

        //Consumer
        Consumer<String> consumer = null;

        //old style
        consumer = new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        };
        consumer.accept("Hello");
        //lambda
        consumer = t -> System.out.println(t);
        consumer.accept("Hai");
        //lambda using method reference
        // Sytem is a class, Stream out is a static variable, println is method
        consumer = System.out::println;
        consumer.accept("Hello method reference");

        //Consumer interface inside list
        List<String> list = Arrays.asList("Subramanian", "Ram", "Kathik");
        //print
        list.forEach(new Consumer<String>() {
            @Override
            public void accept(String item) {
                System.out.println("::"+item);
            }
        });
        list.forEach(name -> System.out.println("--"+name));

        list.forEach(System.out::println);

        list.forEach(LoopPrinter::print);


    }
}
