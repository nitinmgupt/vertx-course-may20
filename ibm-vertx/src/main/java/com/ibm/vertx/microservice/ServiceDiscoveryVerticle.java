package com.ibm.vertx.microservice;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.types.HttpEndpoint;

import javax.xml.ws.Service;

public class ServiceDiscoveryVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    super.start();
    ServiceDiscoveryOptions discoveryOptions = new ServiceDiscoveryOptions();
    discoveryOptions.setBackendConfiguration(new JsonObject().put("connection","127.0.0.1:2181")
      .put("ephemral",true)
      .put("guranteed",true)
      .put("basePath","services/my-backend"));

    ServiceDiscovery serviceDiscovery = ServiceDiscovery.create(vertx,discoveryOptions);

    // Record Creation

    Record httpEndPointRecord = HttpEndpoint.createRecord("http-post-service",true,"jsonplaceholder.typicode.com",443,"/posts", new JsonObject());
    serviceDiscovery.publish(httpEndPointRecord,ar-> {
      if(ar.succeeded()) {
        System.out.println("Successfully published to Zookeeper...>>>>" + ar.result().toJson());
      } else {
        System.out.println(ar.cause());
      }
    });

    // Consume
    // Timer is necessary else it will start discovering/consuming immediately
    vertx.setTimer(5000,ar->{
      //discover service
      HttpEndpoint.getWebClient(serviceDiscovery, new JsonObject().put("name","http-post-service"),sar->{
        //get Resource from discovery server
        if(sar.succeeded()) {
          WebClient client = sar.result();
          client.get("/posts").send(res-> {
            System.out.println("Response is Ready");
            System.out.println(res.result().bodyAsJsonArray());
          });

          //remove/release  discovery record
          ServiceDiscovery.releaseServiceObject(serviceDiscovery,client);
        }
      });
    });
  }
}
