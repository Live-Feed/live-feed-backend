FROM eclipse-temurin:17-focal

WORKDIR /app

ENV active_profile=prod

RUN chmod a+x /app

COPY build/libs/*.jar live-feed-batch.jar

RUN apt-get update && apt-get install -y sudo && \
    sudo apt-get install -y software-properties-common && \
    sudo add-apt-repository universe ppa:mozillateam/ppa \

RUN echo ' \
    Package: * \
    Pin: release o=LP-PPA-mozillateam \
    Pin-Priority: 1001 \
    \
    Package: firefox \
    Pin: version 1:1snap1-0ubuntu2 \
    Pin-Priority: -1 \
    ' | sudo tee /etc/apt/preferences.d/mozilla-firefox

RUN sudo apt-get update && sudo apt-get install -y firefox firefox-geckodriver

RUN echo 'Unattended-Upgrade::Allowed-Origins:: "LP-PPA-mozillateam:${distro_codename}";' | sudo tee /etc/apt/apt.conf.d/51unattended-upgrades-firefox

RUN cp /usr/bin/geckodriver /usr/local/bin/

ENTRYPOINT ["java", "-jar", "/app/live-feed-batch.jar", "--spring.profiles.active=${active_profile}"]
