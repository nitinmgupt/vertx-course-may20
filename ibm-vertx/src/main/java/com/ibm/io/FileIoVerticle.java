package com.ibm.io;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;

public class FileIoVerticle extends AbstractVerticle {

  private void readFile() {
    FileSystem file = vertx.fileSystem();
    //read file operation is handled by kernal threads
    file.readFile("assets/hello.txt", fhandler -> {
      if (fhandler.succeeded()) {
        System.out.println(fhandler.result().toString());
      } else {
        System.out.println(fhandler.cause().toString());
      }
    });
  }

  private Future<String> readFilePromise() {
    Promise promise = Promise.promise();
    FileSystem file = vertx.fileSystem();
    //read file operation is handled by kernal threads
    file.readFile("assets/hello.txt", fhandler -> {
      if (fhandler.succeeded()) {
        promise.complete(fhandler.result().toString());
      } else {
        promise.fail(fhandler.cause().toString());
      }
    });
    return promise.future();
  }

  private Future<String> writeFilePromise() {
    Promise promise = Promise.promise();
    FileSystem file = vertx.fileSystem();
    //write file operation is handled by kernal threads
    file.writeFile("assets/hello_copy.txt", Buffer.buffer("Welcome"), fhandler -> {
      if (fhandler.succeeded()) {
        promise.complete("File has been written successfully!");
      } else {
        promise.fail("File has been written failed!!!!" + fhandler.cause().toString());
      }
    });
    return promise.future();
  }

  @Override
  public void start() throws Exception {
    //i need file content in the start method.
    readFile();
    readFilePromise().onComplete(handler-> {
      if(handler.succeeded()) {
        System.out.println(handler.result());
      } else {
        System.out.println(handler.cause());
      }
    });

    writeFilePromise().onComplete(result-> {
      if(result.succeeded()) {
        System.out.println(result.result());
      } else {
        System.out.println(result.cause());
      }
    });
  }
}
