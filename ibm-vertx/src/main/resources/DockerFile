FROM java:8-jre

ENV VERTICLE_FILE target/vertx-start-project-1.0-SNAPSHOT-fat.jar

# Set the location of the verticles
ENV VERTICLE_HOME /opt/verticles

EXPOSE 8081

COPY $VERTICLE_FILE $VERTICLE_HOME/


WORKDIR $VERTICLE_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["java -jar vertx-start-project-1.0-SNAPSHOT-fat.jar"]
