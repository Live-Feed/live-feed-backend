FROM ghcr.io/live-feed/live-feed-batch-base:0.0.1

WORKDIR /app

RUN chmod a+x /app

COPY build/libs/*.jar live-feed-batch.jar

ENTRYPOINT ["java", "-jar", "/app/live-feed-batch.jar", "--spring.profiles.active=${active_profile}"]
