package com.ibm.vertx.microservice;

import com.fasterxml.jackson.databind.util.JSONPObject;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;

public class FileSystemConfigVerticle  extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    super.start();

    // Config Store Options
    //Add Storage Options   type, format, file path, etc.

    /*ConfigRetrieverOptions configOptions = new ConfigRetrieverOptions();
    configOptions.addStore(new ConfigStoreOptions());*/

    ConfigStoreOptions storeOptions = new ConfigStoreOptions();
    storeOptions.setType("file");
    storeOptions.setFormat("json");
    storeOptions.setConfig(new JsonObject().put("path","application.json"));

    ConfigRetriever retriever = ConfigRetriever.create(vertx,new ConfigRetrieverOptions().addStore(storeOptions));
    retriever.getConfig(config->{
      if (config.succeeded()) {
        System.out.println("Config is ready");
        System.out.println(config.result());
        JsonObject configResult = config.result();
        System.out.println(configResult.getJsonObject("application"));

        System.out.println(configResult.getJsonObject("application").getString("name"));
        System.out.println(configResult.getJsonObject("application").getInteger("port"));

        HttpServerOptions serverConfig = new HttpServerOptions();
        serverConfig.setPort(3001);
        HttpServer server = vertx.createHttpServer(serverConfig);

        server.requestHandler(context -> {
          context.response().putHeader(HttpHeaders.CONTENT_TYPE, "text/html");
          context.response().end("Server Name: " + configResult.getJsonObject("application").getString("name") + "<br>Server Version: " + configResult.getJsonObject("application").getString("Version"));
        });
        //start the httpserver
        server.listen(serverHandler -> {
          if (serverHandler.succeeded()) {
            System.out.println("Server is Up " + serverHandler.result());
          } else {
            System.out.println(serverHandler.cause());
          }
        });

        // try to reformat this using compose()

      } else {
        System.out.println("Config Error: "+config.cause());
      }

    });
  }
}
