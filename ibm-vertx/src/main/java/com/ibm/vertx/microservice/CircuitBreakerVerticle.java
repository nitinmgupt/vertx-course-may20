package com.ibm.vertx.microservice;

import io.vertx.circuitbreaker.CircuitBreaker;
import io.vertx.circuitbreaker.CircuitBreakerOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

public class CircuitBreakerVerticle extends BaseMicroServiceVerticle {

  @Override
  public void start() throws Exception {
    super.start();
    //create circuit breaker object
    /*CircuitBreakerOptions breakerOptions = new CircuitBreakerOptions();
    breakerOptions.setMaxFailures(2); //no. of failures to be allowed, after that circuit will open
    breakerOptions.setTimeout(2000); //consider a timeout if the operation doesn't finish within this time
    breakerOptions.setFallbackOnFailure(true);//if any failure, should i handle fallback or not
    breakerOptions.setResetTimeout(5000); // time spent in open state before attempting to retry

    CircuitBreaker circuitBreaker = CircuitBreaker.create("my-circuit-breaker", vertx, breakerOptions);*/

    //how to employ safty to my code when failure dedecuted

    circuitBreaker.executeWithFallback(
      future -> {
        WebClient client = WebClient.create(vertx);

        // Send a GET request
        client
          .get("jsonplaceholder.typicode.com", "/posts") // wrong api url will open circuit.
          .send(ar -> {
            // Obtain response
            HttpResponse<Buffer> response = ar.result();
            if (response.statusCode() != 200) {
              future.fail("HTTP error");
            } else {
              future.complete(response.bodyAsJsonArray().encode());
            }

          });
      }, v -> {
        // Executed when the circuit is opened
        return "Hello, I am fallback";
      })
      .onComplete(ar -> {
        // Do something with the result
        if (ar.succeeded()) {
          System.out.println(ar.result());
        } else {
          System.out.println(ar.cause());

        }
      });


  }
}
