FROM openjdk:17-jdk

WORKDIR /app

ARG KAFKA_BOOTSTRAP_SERVERS
ARG APPLICATION_NAME
ARG APPLICATION_PORT
ARG DATABASE_URL
ARG DATABASE_USER
ARG DATABASE_PASSWORD

ENV active_profile=prod \
    KAFKA_BOOTSTRAP_SERVERS=$KAFKA_BOOTSTRAP_SERVERS \
    APPLICATION_NAME=$APPLICATION_NAME \
    APPLICATION_PORT=$APPLICATION_PORT \
    DATABASE_URL=$DATABASE_URL \
    DATABASE_USER=$DATABASE_USER \
    DATABASE_PASSWORD=$DATABASE_PASSWORD

RUN chmod a+x /app

COPY build/libs/*.jar crawler.jar

ENTRYPOINT ["java", "-jar", "/app/crawler.jar", "--spring.profiles.active=${active_profile}"]

