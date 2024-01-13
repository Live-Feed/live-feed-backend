FROM oraclelinux:8.9



#ENV JAVA_VERSION=17 \
#    JAVA_HOME=/usr/lib/jvm/java-${JAVA_VERSION}-openjdk \
#    PATH=${JAVA_HOME}/bin:${PATH} \
#    active_profile=prod
#
#RUN dnf install -y java-17-openjdk-devel && \
#    dnf clean all



RUN wget https://download.java.net/java/GA/jdk17.0.2/dfd4a8d0985749f896bed50d7138ee7f/8/GPL/openjdk-17.0.2_linux-aarch64_bin.tar.gz
RUN tar xvf openjdk-17.0.2_linux-aarch64_bin.tar.gz
RUN mv jdk-17.0.2/ /opt/jdk-17/
RUN sudo tee /etc/profile.d/jdk.sh <<EOF && \
    export JAVA_HOME=/opt/jdk-17 && \
    export PATH=\$PATH:\$JAVA_HOME/bin && \
    EOF

RUN source /etc/profile.d/jdk.sh

RUN java --version

WORKDIR /app

RUN chmod a+x /app

COPY build/libs/*.jar live-feed-batch.jar

ENTRYPOINT ["java", "-jar", "/app/live-feed-batch.jar", "--spring.profiles.active=${active_profile}"]
