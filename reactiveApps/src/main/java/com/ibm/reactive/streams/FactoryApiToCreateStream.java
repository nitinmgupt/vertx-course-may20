package com.ibm.reactive.streams;

import io.reactivex.Observable;

import java.util.Arrays;
import java.util.List;

class Employee {
    private int id;
    private String name;

    public Employee() {
        // TODO Auto-generated constructor stub
    }

    public Employee(int id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Employee [id=" + id + ", name=" + name + "]";
    }
}

public class FactoryApiToCreateStream {

    public static void main(String[] args) {
        Observable<Integer> stream = Observable.just(1, 2, 3, 4, 5, 6, 7, 8);
        stream.filter(item -> item % 2 == 0)
                .subscribe(System.out::println, System.out::println, () -> System.out.println("done"));

        //Data Source Array
        String[] names = {"A", "B", "C", "D", "E"};
        Observable<String> namesStream = Observable.fromArray(names);
        namesStream.filter(name -> name == "A")
                .subscribe(System.out::println, System.out::println, () -> System.out.println("done"));

        Employee[] employees = {new Employee(1, "A1"), new Employee(2, "A2"), new Employee(3, "A3")};

        Observable<Employee> employeeStream = Observable.fromArray(employees);

        employeeStream.filter(employee -> employee.getName().equals("A1"))
                .subscribe(System.out::println, System.out::println, () -> System.out.println("done!"));

        //List as Data Source

        List<String> list = Arrays.asList(names);

        Observable<String> listStream = Observable.fromIterable(list);
        listStream.filter(name -> name == "A")
                .subscribe(System.out::println, System.out::println, () -> System.out.println("done"));


    }
}
