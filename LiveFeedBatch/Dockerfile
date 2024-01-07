FROM eclipse-temurin:17-jammy

WORKDIR /app

ENV active_profile=prod

RUN chmod a+x /app

COPY build/libs/*.jar live-feed-batch.jar

RUN apt-get update && \
    apt-get install -y wget unzip snapd && \
    apt-get install -y chromium-driver

RUN snap install chromium

#RUN apt-get install -y chromium-common=112.0.5615.49-0ubuntu0.18.04.1 && \
#    apt-get install -y chromium=112.0.5615.49-0ubuntu0.18.04.1 && \
#    apt-get install -y chromium-driver=112.0.5615.49-0ubuntu0.18.04.1

#RUN CHROMEDRIVER_VERSION=$(wget -qO- https://chromedriver.storage.googleapis.com/LATEST_RELEASE) && \
#    wget -O /tmp/chromedriver.zip https://chromedriver.storage.googleapis.com/$CHROMEDRIVER_VERSION/chromedriver_linux64.zip && \
#    unzip /tmp/chromedriver.zip -d /usr/local/bin/ && \
#    rm /tmp/chromedriver.zip && \
#    chmod +x /usr/local/bin/chromedriver

RUN CHROMEDRIVER_VERSION=$(wget -qO- https://chromedriver.storage.googleapis.com/LATEST_RELEASE) && \
    wget -O /tmp/chromedriver.zip https://chromedriver.storage.googleapis.com/$CHROMEDRIVER_VERSION/chromedriver_linux64.zip && \
    unzip /tmp/chromedriver.zip -d /usr/local/bin/ && \
    rm /tmp/chromedriver.zip && \
    chmod +x /usr/local/bin/chromedriver


#RUN cp /usr/bin/chromedriver /usr/local/bin/

ENTRYPOINT ["java", "-jar", "/app/live-feed-batch.jar", "--spring.profiles.active=${active_profile}"]
