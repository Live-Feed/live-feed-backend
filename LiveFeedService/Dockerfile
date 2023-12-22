FROM openjdk:17-jdk

WORKDIR /app

ENV active_profile=prod

RUN chmod a+x /app

COPY build/libs/*.jar live-feed-service.jar

ENTRYPOINT ["java", "-jar", "/app/live-feed-service.jar", "--spring.profiles.active=${active_profile}"]

