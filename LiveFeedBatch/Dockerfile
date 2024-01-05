FROM amazoncorretto:17

WORKDIR /app

ENV active_profile=prod

RUN chmod a+x /app

COPY build/libs/*.jar live-feed-batch.jar

RUN yum install -y wget

RUN wget https://rpmfind.net/linux/fedora/linux/development/rawhide/Everything/aarch64/os/Packages/c/chromium-common-120.0.6099.129-1.fc40.aarch64.rpm \
    yum localinstall chromium-common-120.0.6099.129-1.fc40.aarch64.rpm \
    wget https://rpmfind.net/linux/fedora/linux/development/rawhide/Everything/aarch64/os/Packages/c/chromium-120.0.6099.129-1.fc40.aarch64.rpm \
    yum localinstall chromium-120.0.6099.129-1.fc40.aarch64.rpm \
    wget https://rpmfind.net/linux/fedora/linux/development/rawhide/Everything/aarch64/os/Packages/c/chromedriver-120.0.6099.129-1.fc40.aarch64.rpm \
    yum localinstall chromedriver-120.0.6099.129-1.fc40.aarch64.rpm

RUN cp /usr/bin/chromedriver /usr/local/bin/

ENTRYPOINT ["java", "-jar", "/app/live-feed-batch.jar", "--spring.profiles.active=${active_profile}"]
