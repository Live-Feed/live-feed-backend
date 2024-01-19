FROM oraclelinux:8.9

# OpenJDK 17 다운로드 및 설치
RUN yum install -y tar gzip wget && \
    wget https://download.java.net/java/GA/jdk17.0.2/dfd4a8d0985749f896bed50d7138ee7f/8/GPL/openjdk-17.0.2_linux-aarch64_bin.tar.gz && \
    tar -zxvf openjdk-17.0.2_linux-aarch64_bin.tar.gz -C /opt && \
    rm openjdk-17.0.2_linux-aarch64_bin.tar.gz

# 환경 변수 설정
ENV JAVA_HOME=/opt/jdk-17.0.2 \
    PATH=$PATH:$JAVA_HOME/bin \
    TZ=Asia/Seoul \
    active_profile=prod

# 시간대 설정
RUN dnf install -y tzdata && \
    ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && \
    echo $TZ > /etc/timezone

# 크롬 관련 패키지 설치
RUN wget https://rpmfind.net/linux/opensuse/ports/aarch64/tumbleweed/repo/oss/aarch64/libminizip1-1.3-1.1.aarch64.rpm && \
    wget https://rpmfind.net/linux/epel/8/Everything/aarch64/Packages/n/nss-mdns-0.14.1-9.el8.aarch64.rpm && \
    wget https://rpmfind.net/linux/epel/testing/8/Everything/aarch64/Packages/c/chromium-common-120.0.6099.224-1.el8.aarch64.rpm && \
    wget https://rpmfind.net/linux/epel/testing/8/Everything/aarch64/Packages/c/chromedriver-120.0.6099.224-1.el8.aarch64.rpm && \
    wget https://rpmfind.net/linux/epel/testing/8/Everything/aarch64/Packages/c/chromium-120.0.6099.224-1.el8.aarch64.rpm && \
    dnf localinstall libminizip1-1.3-1.1.aarch64.rpm && \
    dnf localinstall nss-mdns-0.14.1-9.el8.aarch64.rpm && \
    dnf localinstall chromium-common-120.0.6099.224-1.el8.aarch64.rpm && \
    dnf localinstall chromedriver-120.0.6099.224-1.el8.aarch64.rpm && \
    dnf localinstall chromium-120.0.6099.224-1.el8.aarch64.rpm

WORKDIR /app

RUN chmod a+x /app

COPY build/libs/*.jar live-feed-batch.jar

ENTRYPOINT ["java", "-jar", "/app/live-feed-batch.jar", "--spring.profiles.active=${active_profile}"]
