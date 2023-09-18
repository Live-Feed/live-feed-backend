FROM openjdk:17-jdk

WORKDIR /app

ARG KAFKA_BOOTSTRAP_SERVERS
ARG APPLICATION_NAME
ARG APPLICATION_PORT

ENV active_profile=prod \
    KAFKA_BOOTSTRAP_SERVERS=$KAFKA_BOOTSTRAP_SERVERS \
    APPLICATION_NAME=$APPLICATION_NAME \
    APPLICATION_PORT=$APPLICATION_PORT

RUN chmod a+x /app

COPY build/libs/*.jar parser.jar

ENTRYPOINT ["java", "-jar", "/app/parser.jar", "--spring.profiles.active=${active_profile}"]

