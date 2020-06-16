package com.ibm.vertx.func;

//How to pass instance methods and static methods as parameter.

@FunctionalInterface
interface Printer {
    void Print(String name);
}

class MicroTaskExecutor {
    public static void startMicroTask() {
        for (int i = 0; i < 5; i++) {
            System.out.println(MicroTaskExecutor.class.getName() + " " + i);
        }
    }
}

//class holding instance method
class Loop {

    //instance method to be passed as parameter
    private void startMicroTask() {
        for (int i = 0; i < 5; i++) {
            System.out.println("Micro Task Thread -" + i);
        }
    }

    public void startLoop() {
        Thread thread = null;
        //old style
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    System.out.println("Thread -" + i);
                }
            }
        });
        thread.start();
        //lambda : passing function as parameter
        thread = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("Thread -" + i);
            }
        });
        thread.start();
        //Sample s = new Sample();
        //lambda : using method reference : pass a separate function as parameter
        // thread = new Thread(() -> s.startMicroTask());
        thread = new Thread(() -> startMicroTask());
        thread.start();
        //lambda : using method reference symbol "::"
        thread = new Thread(this::startMicroTask);
        thread.start();
        //lambda : using static methods
        thread = new Thread(MicroTaskExecutor::startMicroTask);
        thread.start();


    }
}


public class MethodReferenceDemo {
    public static void main(String[] args) {
        Loop loop = new Loop();
        loop.startLoop();
        //method references inside java :  System.out.println
        Printer printer = null;

        //l=old lambda style
        printer = name -> {
            System.out.println(name);
        };
        printer.Print("Subramanian");
        //using method reference :
        //System - class ; static PrintStream out-static variable ; println - mehtod of out variable
        printer = System.out::println;
        printer.Print("Geetha Subramanian");

    }

}
