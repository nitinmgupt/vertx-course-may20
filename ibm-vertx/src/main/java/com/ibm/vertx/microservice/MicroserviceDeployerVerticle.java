package com.ibm.vertx.microservice;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;

public class MicroserviceDeployerVerticle extends AbstractVerticle {
  @Override
  public void start() throws Exception {
    super.start();
    //Config Within Verticle
    //vertx.deployVerticle(new FileSystemConfigVerticle());

    //Config Outside Verticle
    // JSON way
/*    ConfigStoreOptions storeOptions = new ConfigStoreOptions();
    storeOptions.setType("file");
    storeOptions.setFormat("json"); // default is JSON
    storeOptions.setConfig(new JsonObject().put("path", "application.json"));

    ConfigRetriever retriever = ConfigRetriever.create(vertx, new ConfigRetrieverOptions().addStore(storeOptions));
    retriever.getConfig(config -> {
      if (config.succeeded()) {
        JsonObject configResult = config.result().getJsonObject("application");

        vertx.deployVerticle(new FileSystemConfigOutsideVerticle(), new DeploymentOptions().setConfig(configResult));
      }
    });*/

    //Config Outside Verticle
    //YAML way
/*    ConfigStoreOptions storeOptionsYaml = new ConfigStoreOptions();
    storeOptionsYaml.setType("file");
    storeOptionsYaml.setFormat("yaml"); // default is JSON
    storeOptionsYaml.setConfig(new JsonObject().put("path", "application.yaml"));

    ConfigRetriever retrieverYaml = ConfigRetriever.create(vertx, new ConfigRetrieverOptions().addStore(storeOptionsYaml));

    retrieverYaml.getConfig(yamlProps-> {
      System.out.println("YAML Properties");
      System.out.println(yamlProps.result());
      vertx.deployVerticle(new FileSystemConfigOutsideVerticle(), new DeploymentOptions().setConfig(yamlProps.result()));

    });*/

    /*vertx.deployVerticle(new ThirdPartyRESTCallVerticle());*/

    /*vertx.deployVerticle(new ThirdPartyBackendCallVerticle());*/

    /*vertx.deployVerticle(new ServiceDiscoveryVerticle());*/

    vertx.deployVerticle(new ReactiveHTTPServer());
    vertx.deployVerticle(new RequestFromReactiveClient());


  }
}
