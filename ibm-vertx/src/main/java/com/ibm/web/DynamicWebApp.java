package com.ibm.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.templ.freemarker.FreeMarkerTemplateEngine;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.github.rjeschke.txtmark.Processor;


public class DynamicWebApp extends AbstractVerticle {

  private static final String SQL_CREATE_PAGES_TABLE = "create table if not exists Pages (Id integer identity primary key, Name varchar(255) unique, Content clob)";
  private static final String SQL_GET_PAGE = "select Id, Content from Pages where Name = ?"; // <1>
  private static final String SQL_CREATE_PAGE = "insert into Pages values (NULL, ?, ?)";
  private static final String SQL_SAVE_PAGE = "update Pages set Content = ? where Id = ?";
  private static final String SQL_ALL_PAGES = "select Name from Pages";
  private static final String SQL_DELETE_PAGE = "delete from Pages where Id = ?";

  private JDBCClient dbClient;

  private static final Logger LOGGER = LoggerFactory.getLogger(io.vertx.starter.MainVerticle.class);


  private FreeMarkerTemplateEngine templateEngine;


  private Future<Void> prepareDatabase() {

    Promise<Void> promise = Promise.promise();

    //connection information
    dbClient = JDBCClient.createShared(vertx, new JsonObject()  // <1>
      .put("url", "jdbc:hsqldb:file:db/wiki")   // <2>
      .put("driver_class", "org.hsqldb.jdbcDriver")   // <3>
      .put("max_pool_size", 30));   // <4>

    //db inital setup
    dbClient.getConnection(ar -> {    // <5>
      if (ar.failed()) {
        LOGGER.error("Could not open a database connection", ar.cause());
        System.out.println("Could not open a database connection" + ar.cause());
        promise.fail(ar.cause());    // <6>

      } else {
        System.out.println("Database connection is successful");
        SQLConnection connection = ar.result();   // <7>

        connection.execute(SQL_CREATE_PAGES_TABLE, create -> {
          connection.close();   // <8>
          if (create.failed()) {
            LOGGER.error("Database preparation error", create.cause());
            System.out.println("Database preparation error"+ create.cause());
            promise.fail(create.cause());
          } else {
            System.out.println("Database preparation successful" + create.result());
            promise.complete();  // <9>
          }
        });
      }
    });

    return promise.future();
  }

  private void pageDeletionHandler(RoutingContext context) {
    String id = context.request().getParam("id");
    dbClient.getConnection(car -> {
      if (car.succeeded()) {
        SQLConnection connection = car.result();
        connection.updateWithParams(SQL_DELETE_PAGE, new JsonArray().add(id), res -> {
          connection.close();
          if (res.succeeded()) {
            context.response().setStatusCode(303);
            context.response().putHeader("Location", "/");
            context.response().end();
          } else {
            context.fail(res.cause());
          }
        });
      } else {
        context.fail(car.cause());
      }
    });
  }
  // end::pageDeletionHandler[]

  // tag::pageCreateHandler[]
  private void pageCreateHandler(RoutingContext context) {
    String pageName = context.request().getParam("name");
    String location = "/wiki/" + pageName;
    if (pageName == null || pageName.isEmpty()) {
      location = "/";
    }
    context.response().setStatusCode(303);
    context.response().putHeader("Location", location);
    context.response().end();
  }
  // end::pageCreateHandler[]

  // tag::indexHandler[]
  //RoutingContext => ServletContext in servlet--->Request and Reponse
  //context.put("key","value") - App level data sharing
  private void indexHandler(RoutingContext context) {
    System.out.println("index page handler called");
    dbClient.getConnection(car -> {
      if (car.succeeded()) {
        System.out.println("indexHandler::Connection successful");
        SQLConnection connection = car.result();
        connection.query(SQL_ALL_PAGES, res -> {
          connection.close();

          if (res.succeeded()) {
            //java streams -replace with RX-JAVA improve performance
            List<String> pages = res.result() // <1>
              .getResults()
              .stream()
              .map(json -> json.getString(0))
              .sorted()
              .collect(Collectors.toList());

            //this will be accessed inside
            context.put("title", "Wiki home");  // <2>
            context.put("pages", pages);

            templateEngine.render(context.data(), "templates/index.ftl", ar -> {   // <3>
              if (ar.succeeded()) {
                System.out.println("indexHandler:: Rendering successful"+ ar.result());
                context.response().putHeader("Content-Type", "text/html");
                context.response().end(ar.result());  // <4>
              } else {
                System.out.println("indexHandler:: Rendering failed" + ar.cause());
                context.fail(ar.cause());
              }
            });

          } else {
            context.fail(res.cause());  // <5>
          }
        });
      } else {
        System.out.println("indexHandler::Connection Failed " + car.cause());
        context.fail(car.cause());
      }
    });
  }
  // end::indexHandler[]

  // tag::pageUpdateHandler[]
  //RoutingContext ---which encasulate -HttpRequest Object and HttpResponse
  private void pageUpdateHandler(RoutingContext context) {
    //getParam - to read url params /id ?id
    String id = context.request().getParam("id");   // <1>
    String title = context.request().getParam("title");
    String markdown = context.request().getParam("markdown");

    boolean newPage = "yes".equals(context.request().getParam("newPage"));  // <2>

    dbClient.getConnection(car -> {
      if (car.succeeded()) {
        SQLConnection connection = car.result();
        String sql = newPage ? SQL_CREATE_PAGE : SQL_SAVE_PAGE;
        JsonArray params = new JsonArray();   // <3>
        if (newPage) {
          params.add(title).add(markdown);
        } else {
          params.add(markdown).add(id);
        }
        connection.updateWithParams(sql, params, res -> {   // <4>
          connection.close();
          if (res.succeeded()) {
            context.response().setStatusCode(303);    // <5>
            context.response().putHeader("Location", "/wiki/" + title);
            context.response().end();
          } else {
            context.fail(res.cause());
          }
        });
      } else {
        context.fail(car.cause());
      }
    });
  }
  // end::pageUpdateHandler[]

  // tag::pageRenderingHandler[]
  private static final String EMPTY_PAGE_MARKDOWN =
    "# A new page\n" +
      "\n" +
      "Feel-free to write in Markdown!\n";

  private void pageRenderingHandler(RoutingContext context) {
    String page = context.request().getParam("page");   // <1>

    dbClient.getConnection(car -> {
      if (car.succeeded()) {

        SQLConnection connection = car.result();
        connection.queryWithParams(SQL_GET_PAGE, new JsonArray().add(page), fetch -> {  // <2>
          connection.close();
          if (fetch.succeeded()) {

            JsonArray row = fetch.result().getResults()
              .stream()
              .findFirst()
              .orElseGet(() -> new JsonArray().add(-1).add(EMPTY_PAGE_MARKDOWN));
            Integer id = row.getInteger(0);
            String rawContent = row.getString(1);

            context.put("title", page);
            context.put("id", id);
            context.put("newPage", fetch.result().getResults().size() == 0 ? "yes" : "no");
            context.put("rawContent", rawContent);
            context.put("content", Processor.process(rawContent));  // <3>
            context.put("timestamp", new Date().toString());

            templateEngine.render(context.data(), "templates/page.ftl", ar -> {
              if (ar.succeeded()) {
                context.response().putHeader("Content-Type", "text/html");
                context.response().end(ar.result());
              } else {
                context.fail(ar.cause());
              }
            });
          } else {
            context.fail(fetch.cause());
          }
        });

      } else {
        context.fail(car.cause());
      }
    });
  }

  private Future<Void> startHttpServer() {
    Promise<Void> promise = Promise.promise();

    HttpServer server = vertx.createHttpServer();   // <1>

    Router router = Router.router(vertx);   // <2>

    router.get("/").handler(this::indexHandler);
    router.get("/wiki/:page").handler(this::pageRenderingHandler); // <3>
    router.post().handler(BodyHandler.create());  // <4>
    router.post("/save").handler(this::pageUpdateHandler);
    router.post("/create").handler(this::pageCreateHandler);
    router.post("/delete").handler(this::pageDeletionHandler);

    templateEngine = FreeMarkerTemplateEngine.create(vertx);

    server
      .requestHandler(router)   // <5>
      .listen(8011, ar -> {   // <6>
        if (ar.succeeded()) {
          System.out.println("HTTP server running on port 8011. Try to access Wiki app http://localhost:8011");
          LOGGER.info("HTTP server running on port 8011");
          promise.complete();
        } else {
          LOGGER.error("Could not start a HTTP server", ar.cause());
          promise.fail(ar.cause());
        }
      });

    return promise.future();
  }

  @Override
  public void start(Promise<Void> promise) throws Exception {
    Future<Void> steps = prepareDatabase().compose(v -> startHttpServer());
    steps.onComplete(promise);
  }

  public void anotherStart(Promise<Void> promise) throws Exception {
    // tag::another-start[]
    Future<Void> steps = prepareDatabase().compose(v -> startHttpServer());
    steps.setHandler(ar -> {  // <1>
      if (ar.succeeded()) {
        promise.complete();
      } else {
        promise.fail(ar.cause());
      }
    });
    // end::another-start[]
  }
}
