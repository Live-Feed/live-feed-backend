FROM oraclelinux:8.9

WORKDIR /app

ENV JAVA_VERSION=17 \
    JAVA_HOME=/usr/lib/jvm/java-${JAVA_VERSION}-openjdk \
    PATH=${JAVA_HOME}/bin:${PATH} \
    active_profile=prod

RUN dnf install -y java-17-openjdk-devel && \
    dnf clean all

RUN chmod a+x /app

COPY build/libs/*.jar live-feed-batch.jar

ENTRYPOINT ["java", "-jar", "/app/live-feed-batch.jar", "--spring.profiles.active=${active_profile}"]
