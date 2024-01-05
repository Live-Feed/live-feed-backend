FROM openjdk:17-jdk

WORKDIR /app

ENV active_profile=prod

RUN chmod a+x /app

COPY build/libs/*.jar live-feed-batch.jar

RUN apt install -y \
    chromium-common=112.0.5615.49-0ubuntu0.18.04.1 \
    chromium=112.0.5615.49-0ubuntu0.18.04.1 \
    chromium-driver=112.0.5615.49-0ubuntu0.18.04.1

RUN cp /usr/bin/chromedriver /usr/local/bin/

ENTRYPOINT ["java", "-jar", "/app/live-feed-batch.jar", "--spring.profiles.active=${active_profile}"]
