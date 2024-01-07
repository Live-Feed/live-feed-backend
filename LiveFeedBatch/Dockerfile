FROM eclipse-temurin:17-jammy

WORKDIR /app

ENV active_profile=prod

RUN chmod a+x /app

COPY build/libs/*.jar live-feed-batch.jar

#RUN apt install -y \
#    chromium-common=112.0.5615.49-0ubuntu0.18.04.1 \
#    chromium=112.0.5615.49-0ubuntu0.18.04.1 \
#    chromium-driver=112.0.5615.49-0ubuntu0.18.04.1

RUN wget https://rpmfind.net/linux/fedora/linux/development/rawhide/Everything/aarch64/os/Packages/c/chromedriver-120.0.6099.199-1.fc40.aarch64.rpm
RUN rpm -ivh chromedriver-120.0.6099.199-1.fc40.aarch64

# RUN cp /usr/bin/chromedriver /usr/local/bin/

ENTRYPOINT ["java", "-jar", "/app/live-feed-batch.jar", "--spring.profiles.active=${active_profile}"]
