FROM openjdk:17-jdk

WORKDIR /app

ARG profile
ENV active_profile=prod \
    KAFKA_BOOTSTRAP_SERVERS=$KAFKA_BOOTSTRAP_SERVERS \
    CRAWLER_PORT=$CRAWLER_PORT

RUN chmod a+x /app

COPY build/libs/**.jar .crawler.jar

ENTRYPOINT ["java", "-jar", "/app/crawler.jar", "--spring.profiles.active=${active_profile}"]

