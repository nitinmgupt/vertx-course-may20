package com.ibm.vertx.microservice;

import io.vertx.circuitbreaker.CircuitBreaker;
import io.vertx.circuitbreaker.CircuitBreakerOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.impl.ConcurrentHashSet;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.types.EventBusService;
import io.vertx.servicediscovery.types.HttpEndpoint;
import io.vertx.servicediscovery.types.JDBCDataSource;
import io.vertx.servicediscovery.types.MessageSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public abstract class BaseMicroServiceVerticle extends AbstractVerticle {

  private static final String LOG_EVENT_ADDRESS = "events.log";

  private static final Logger logger = LoggerFactory.getLogger(BaseMicroServiceVerticle.class);

  protected ServiceDiscovery discovery;
  protected CircuitBreaker circuitBreaker;
  protected Set<Record> registeredRecords = new ConcurrentHashSet<>();

  @Override
  public void start() throws Exception {
    super.start();
    CircuitBreakerOptions breakerOptions = new CircuitBreakerOptions();
    breakerOptions.setMaxFailures(2); //no. of failures to be allowed, after that circuit will open
    breakerOptions.setTimeout(3000); //consider a timeout if the operation doesn't finish within this time
    breakerOptions.setFallbackOnFailure(true);//if any failure, should i handle fallback or not
    breakerOptions.setResetTimeout(5000); // time spent in open state before attempting to retry

    circuitBreaker = CircuitBreaker.create("my-circuit-breaker", vertx, breakerOptions);

    System.out.println("BASE CLASS initalized");
    // init service discovery instance
    ServiceDiscoveryOptions discoveryOptions = new ServiceDiscoveryOptions();
    discoveryOptions.setBackendConfiguration(new JsonObject().put("connection", "localhost:2181")
      .put("ephemeral", true).put("guaranteed", true).put("basePath", "/services/my-backend"));

    discovery = ServiceDiscovery.create(vertx, discoveryOptions);
  }

  protected Future<Void> publishHttpEndpoint(String name, String host, int port) {
    System.out.println("publish http end point");
    //Record record = HttpEndpoint.createRecord(name, host, port,new JsonObject().put("test",""));
    Record record = HttpEndpoint.createRecord(name, host, port, "/",
      new JsonObject().put("api.name", config().getString("api.name", ""))
    );
    System.out.println(config().getString("api.name", "default value"));
    System.out.println(record);
    return publish(record);
  }

  protected Future<Void> publishHttpEndpoint(String name, boolean ssl, String host, int port) {
    System.out.println("publish http end point");
    //Record record = HttpEndpoint.createRecord(name, host, port,new JsonObject().put("test",""));
    Record record = HttpEndpoint.createRecord(name, ssl, host, port, "/",
      new JsonObject().put("api.name", config().getString("api.name", ""))
    );
    System.out.println(config().getString("api.name", "default value"));
    System.out.println(record);
    return publish(record);
  }

  private Future<Void> publish(Record record) {
    System.out.println("Inside publish record");
    if (discovery == null) {
      try {
        System.out.println("Discovery is null");
        start();
      } catch (Exception e) {
        throw new IllegalStateException("Cannot create discovery service");
      }
    }

    Future<Void> future = Future.future();
    // publish the service
    discovery.publish(record, ar -> {
      System.out.println("Inside publish record -success");

      if (ar.succeeded()) {
        registeredRecords.add(record);
        logger.info("Service <" + ar.result().getName() + ">successfully published to zookeeper");
        System.out.println(registeredRecords.size() + " Records Published");
        future.complete();
      } else {
        future.fail("failded to create");
      }
    });

    return future;
  }

  protected Future<Void> publishMessageSource(String name, String address) {
    Record record = MessageSource.createRecord(name, address);
    return publish(record);
  }

  protected Future<Void> publishJDBCDataSource(String name, JsonObject location) {
    Record record = JDBCDataSource.createRecord(name, location, new JsonObject());
    return publish(record);
  }

  protected Future<Void> publishEventBusService(String name, String address, Class serviceClass) {
    Record record = EventBusService.createRecord(name, address, serviceClass);
    return publish(record);
  }

  protected void publishLogEvent(String type, JsonObject data) {
    JsonObject msg = new JsonObject().put("type", type)
      .put("message", data);
    vertx.eventBus().publish(LOG_EVENT_ADDRESS, msg);
  }

  protected void publishLogEvent(String type, JsonObject data, boolean succeeded) {
    JsonObject msg = new JsonObject().put("type", type)
      .put("status", succeeded)
      .put("message", data);
    vertx.eventBus().publish(LOG_EVENT_ADDRESS, msg);
  }

  @Override
  public void stop(Future<Void> future) throws Exception {
    List<Future> futures = new ArrayList<>();
    registeredRecords.forEach(record -> {
      Future<Void> cleanupFuture = Future.future();
      futures.add(cleanupFuture);
      discovery.unpublish(record.getRegistration(), cleanupFuture.completer());
    });

    if (futures.isEmpty()) {
      discovery.close();
      future.complete();
    } else {
      CompositeFuture.all(futures)
        .setHandler(ar -> {
          discovery.close();
          if (ar.failed()) {
            future.fail(ar.cause());
          } else {
            future.complete();
          }
        });
    }
  }
}

