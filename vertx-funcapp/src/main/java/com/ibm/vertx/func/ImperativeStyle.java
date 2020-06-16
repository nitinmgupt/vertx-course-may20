package com.ibm.vertx.func;

import org.omg.Messaging.SyncScopeHelper;

import java.util.Arrays;
import java.util.List;

public class ImperativeStyle {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        //find the total of double of even numbers
        int result =0; //state variable
        //loop
        for(int e: numbers){
            //check ; condition to test even numbers
            if(e%2==0){
                //update the state : mutate the state
                result += e*2;
            }
        }
        System.out.println("The Result is " + result);
        List<String> names = Arrays.asList("Dory", "Gill", "Bruce", "Nemo", "Darla", "Marlin", "Jacques");

       // Problem statement : find Nemo.
        // imperative
        boolean found = false;

        // using for loop
        for (int i = 0; i < names.size(); i++) {
            //condition
            if (names.get(i).equals("Nemo")) {
                found = true;
                break;
            }
        }
        //for each
        for (String name : names) {
            if (name.equals("Nemo")) {
                found = true;
                break;
            }
        }
        if (found)
            System.out.println("Nemo found");
        else
            System.out.println("Nemo not found");
    }
}
