package com.ibm.vertx.func;


@FunctionalInterface
interface Variable {
    void doIt();
}

public class VariableCaptureDemo {
    //instance variable
    private int counter;

    //capturing instance variable inside lambda
    public void increment() {
        Variable variable = () -> {
            //instance variable is captured
            System.out.println(++counter);
        };
        variable.doIt();
    }

    public static void main(String[] args) {
        new VariableCaptureDemo().increment();
        //local variable
        String name="Subramanian";
        //Capturing a local variable inside lambda
        Variable variable = ()->{
            System.out.println(name);
        };
        variable.doIt();

    }

}
