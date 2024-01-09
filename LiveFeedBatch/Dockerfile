FROM eclipse-temurin:17-focal

WORKDIR /app

ENV active_profile=prod

RUN chmod a+x /app

COPY build/libs/*.jar live-feed-batch.jar

RUN apt-get update && apt-get install -y sudo && \
    sudo apt-get install -y software-properties-common && \
    sudo add-apt-repository security

RUN sudo apt-get update && sudo apt-get install -y firefox firefox-geckodriver

RUN cp /usr/bin/geckodriver /usr/local/bin/

ENTRYPOINT ["java", "-jar", "/app/live-feed-batch.jar", "--spring.profiles.active=${active_profile}"]
