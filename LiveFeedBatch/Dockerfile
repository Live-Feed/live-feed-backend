FROM openjdk:17-jdk

WORKDIR /app

RUN chmod a+x /app

COPY build/libs/*.jar live-feed-batch.jar

ENTRYPOINT ["java", "-jar", "/app/crawler.jar", "--spring.profiles.active=${active_profile}"]

