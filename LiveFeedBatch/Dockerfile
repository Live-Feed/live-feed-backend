FROM eclipse-temurin:17-jammy

WORKDIR /app

ENV active_profile=prod

RUN chmod a+x /app

COPY build/libs/*.jar live-feed-batch.jar

#RUN apt-get update && \
#    apt-get install -y wget unzip chromium-driver

RUN apt-get update && apt-get install -y sudo && \
    sudo apt-get install -y software-properties-common snapd && \
    sudo add-apt-repository universe \

RUN sudo apt-get update && sudo apt-get install -yqq daemonize dbus-user-session fontconfig \
    sudo daemonize /usr/bin/unshare --fork --pid --mount-proc /lib/systemd/systemd --system-unit=basic.target \
    exec sudo nsenter -t $(pidof systemd) -a su - $LOGNAME

RUN apt-get update && sudo snap install chromium && apt-get install -y chromium-browser chromium-chromedriver

#RUN CHROMEDRIVER_VERSION=$(wget -qO- https://chromedriver.storage.googleapis.com/LATEST_RELEASE) && \
#    wget -O /tmp/chromedriver.zip https://chromedriver.storage.googleapis.com/$CHROMEDRIVER_VERSION/chromedriver_linux64.zip && \
#    unzip /tmp/chromedriver.zip -d /usr/local/bin/ && \
#    rm /tmp/chromedriver.zip && \
#    chmod +x /usr/local/bin/chromedriver

#RUN CHROMEDRIVER_VERSION=$(wget -qO- https://chromedriver.storage.googleapis.com/LATEST_RELEASE) && \
#    wget -O /tmp/chromedriver.zip https://chromedriver.storage.googleapis.com/$CHROMEDRIVER_VERSION/chromedriver_linux64.zip && \
#    unzip /tmp/chromedriver.zip -d /usr/local/bin/ && \
#    rm /tmp/chromedriver.zip && \
#    chmod +x /usr/local/bin/chromedriver

RUN cp /usr/bin/chromedriver /usr/local/bin/

ENTRYPOINT ["java", "-jar", "/app/live-feed-batch.jar", "--spring.profiles.active=${active_profile}"]
