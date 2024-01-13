FROM oraclelinux:8.9



#ENV JAVA_VERSION=17 \
#    JAVA_HOME=/usr/lib/jvm/java-${JAVA_VERSION}-openjdk \
#    PATH=${JAVA_HOME}/bin:${PATH} \
#    active_profile=prod
#
#RUN dnf install -y java-17-openjdk-devel && \
#    dnf clean all

# 시스템 업데이트 및 필요한 패키지 설치
RUN yum update -y && yum install -y curl tar gzip

# OpenJDK 17 다운로드 및 설치
RUN wget https://download.java.net/java/early_access/alpine/11/binaries/openjdk-17-ea+11_linux-aarch64_bin.tar.gz
RUN tar -zxvf openjdk-17-ea+11_linux-aarch64_bin.tar.gz -C /opt
RUN rm openjdk-17-ea+11_linux-aarch64_bin.tar.gz

# 환경 변수 설정
ENV JAVA_HOME=/opt/jdk-17
ENV PATH=$PATH:$JAVA_HOME/bin

# 실행 명령어
CMD ["java", "-version"]

#RUN source /etc/profile.d/jdk.sh
#
#RUN java --version
#
#WORKDIR /app
#
#RUN chmod a+x /app
#
#COPY build/libs/*.jar live-feed-batch.jar
#
#ENTRYPOINT ["java", "-jar", "/app/live-feed-batch.jar", "--spring.profiles.active=${active_profile}"]
