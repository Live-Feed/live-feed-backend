FROM openjdk:17-jdk

WORKDIR /app

ENV active_profile=prod

RUN chmod a+x /app

COPY build/libs/*.jar live-feed-batch.jar

RUN apk add --no-cache chromium chromium-chromedriver

RUN cp /usr/bin/chromedriver /usr/local/bin/

ENTRYPOINT ["java", "-jar", "/app/live-feed-batch.jar", "--spring.profiles.active=${active_profile}"]
