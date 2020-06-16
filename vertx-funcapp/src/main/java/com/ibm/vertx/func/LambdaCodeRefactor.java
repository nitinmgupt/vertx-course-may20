package com.ibm.vertx.func;

@FunctionalInterface
interface Stock {
    void printStock();
}

@FunctionalInterface
interface Calculator {
    void calculate(int a, int b);
}

@FunctionalInterface
interface Accept {
    void setValue(String value);
}

@FunctionalInterface
interface Adder {
    int add(int a, int b);
}

@FunctionalInterface
interface Mirror {
    String getValue(String value);
}

public class LambdaCodeRefactor {
    public static void main(String[] args) {

//        Stock stock = ()->{
//            System.out.println("Stock Value is 1000");
//        };

        //Use case 1: if the lambda function contains only one line of body.
        //drop {}
        Stock stock = () -> System.out.println("Stock Value is 1000");
        stock.printStock();

        //Use case 2 : args and parameters
        //args are a, b
        Calculator calculator = (int a, int b) -> {
            int c = a + b;
            System.out.println("Calcualtor result " + c);
        };
        //parameters
        calculator.calculate(10, 10);
        //Use case 2.1: args can omit data type.
        //Type inference; Arg type is understood based on parameter value.
        Calculator calc = (a, b) -> {
            int c = a + b;
            System.out.println("Calcualtor result " + c);
        };
        calc.calculate(10, 10);

        //Use 2.2 : if single args, drop () itself
        Accept accept = name -> System.out.println(name);
        accept.setValue("Subramanian!!");

        //Use 3 : if you are returning value
        Adder adder = (a, b) -> {
            return a + b;
        };
        System.out.println(adder.add(10, 10));

        //Use 3.1. if only return statement ; remove return statement and {}
        Adder adder1 = (a, b) -> a + b;
        System.out.println(adder1.add(10, 10));

        //Use case 3.2. only one args , return the same ,
        Mirror mirror = value -> value;
        System.out.println(mirror.getValue("Hello Mirror"));

    }
}
