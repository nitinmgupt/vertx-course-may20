package com.ibm.io;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;
import io.vertx.core.json.JsonObject;


//Service class
class FileService {
  static Vertx vertx = Vertx.vertx();

  public static Future<String> readFile() {
    Promise promise = Promise.promise();

    FileSystem file = vertx.fileSystem();
    //read file operation is handled by kernal threads
    file.readFile("assets/hello.txt", fhandler -> {
      if (fhandler.succeeded()) {
        //System.out.println(fhandler.result().toString());
        promise.complete(fhandler.result().toString());
      } else {
        //System.out.println(fhandler.cause().toString());
        promise.fail(fhandler.cause().getMessage());
      }
    });
    return promise.future();
  }

  public static Future<String> writeFile() {
    Promise promise = Promise.promise();

    FileSystem file = vertx.fileSystem();
    file.writeFile("assets/hello_copy.txt", Buffer.buffer("Welcome"), result -> {
      if (result.succeeded()) {
        promise.complete("File has been written succesfully!");
      } else {
        promise.fail(result.cause().toString());
      }
    });

    return promise.future();

  }

  public static void createBuffer() {

    Buffer buffer = Buffer.buffer();
    buffer.appendString("Hello");
    buffer.appendByte((byte) 127);
    buffer.appendShort((short) 127);
    buffer.appendInt(127);
    buffer.appendLong(127);
    buffer.appendFloat(127.0F);
    buffer.appendDouble(127.0D);
    System.out.println("buffer.length() = " + buffer.length());
    for (int i = 0; i < buffer.length(); i += 4) {
      System.out.println("int value at " + i + " is " + buffer.getInt(i));
    }
  }

  public static Future<JsonObject> processJSON() {
    Promise<JsonObject> promise = Promise.promise();
    JsonObject jsonObject = new JsonObject();
    jsonObject.put("id", 1);
    jsonObject.put("name", "Subramanian");
    jsonObject.put("location", "Coimbatore");
    promise.complete(jsonObject);
    return promise.future();

  }

}


public class FileIoVerticle2 extends AbstractVerticle {
  @Override
  public void start() throws Exception {
    //i need file content in the start method.
    FileService.readFile().onComplete(h -> {
      if (h.succeeded()) {
        System.out.println(h.result());
      }
    });
    FileService.writeFile().onComplete(h -> {
      if (h.succeeded()) {
        System.out.println(h.result());
      } else {
        System.out.println(h.cause());
      }
    });
    FileService.createBuffer();
    FileService.processJSON().onComplete(h -> {
      System.out.println(h.result().encodePrettily());
    });


  }
}
