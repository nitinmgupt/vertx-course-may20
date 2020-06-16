package com.ibm.vertx.func;


@FunctionalInterface
interface Handler {
    void handle();
}

@FunctionalInterface
interface AsyncHandler {
    void handle(String connectionString);
}

@FunctionalInterface
interface Resolver {
    void resolve(String message);
}

@FunctionalInterface
interface Rejection {
    void reject(String error);
}


//domain class which is having method, where we pass function as parameter
class Connector {

    public void connect(Handler handler) {
        //call handle method
        handler.handle();
    }

    public void asynConnect(AsyncHandler handler) {
        //pass parameter to the handlerfunction
        handler.handle("redis:localhost:3333/mydb");
    }
}

class HttpServer {
    public void start(Resolver resolver, Rejection rejection) {

        //bizlogic
        boolean isConnected = true;
        if (isConnected) {
            resolver.resolve("HTTP Server Connected and Ready to accept Incomming Request!");
        } else {
            rejection.reject("Server Connection failed");
        }
    }
}


public class FunctionAsParameter {
    public static void main(String[] args) {

        //connector
        Connector connector = new Connector();
        //old java style
        connector.connect(new Handler() {
            @Override
            public void handle() {
                System.out.println("Connection is Ready!");
            }
        });

        //using lambda :passing function as parameter
        connector.connect(() -> System.out.println("Connection is Ready inside lambda"));

        //get the parameter
        connector.asynConnect(connectionStr -> System.out.println(connectionStr));

        //HttpServer
        HttpServer httpServer = new HttpServer();
        //pass function as parameter
        httpServer.start(message -> System.out.println(message), error -> System.out.println(error));
        
        httpServer.start(new Resolver() {
            @Override
            public void resolve(String message) {
                System.out.println(message);
            }
        }, new Rejection() {
            @Override
            public void reject(String error) {
                System.out.println(error);
            }
        });


    }
}
