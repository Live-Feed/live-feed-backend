FROM oraclelinux:8.9

#ENV JAVA_VERSION=17 \
#    JAVA_HOME=/usr/lib/jvm/java-${JAVA_VERSION}-openjdk \
#    PATH=${JAVA_HOME}/bin:${PATH} \
#    active_profile=prod
#
#RUN dnf install -y java-17-openjdk-devel && \
#    dnf clean all

# 시스템 업데이트 및 필요한 패키지 설치
RUN yum install -y tar gzip wget

# OpenJDK 17 다운로드 및 설치
#RUN wget https://download.java.net/java/GA/jdk17.0.2/dfd4a8d0985749f896bed50d7138ee7f/8/GPL/openjdk-17.0.2_linux-aarch64_bin.tar.gz && \
#    tar -zxvf openjdk-17.0.2_linux-aarch64_bin.tar.gz -C /opt && \
#    rm openjdk-17.0.2_linux-aarch64_bin.tar.gz

# 환경 변수 설정
#ENV JAVA_HOME=/opt/jdk-17.0.2
#ENV PATH=$PATH:$JAVA_HOME/bin
ENV active_profile=prod

# 실행 명령어
#CMD ["java", "--version"]

#RUN source /etc/profile.d/jdk.sh
#
#RUN java --version

RUN dnf install -y sudo libminizip && \
    wget https://rpmfind.net/linux/epel/8/Everything/aarch64/Packages/n/nss-mdns-0.14.1-9.el8.aarch64.rpm && \
    wget https://rpmfind.net/linux/epel/testing/8/Everything/aarch64/Packages/c/chromium-common-120.0.6099.216-1.el8.aarch64.rpm && \
    wget https://rpmfind.net/linux/epel/testing/8/Everything/aarch64/Packages/c/chromedriver-120.0.6099.216-1.el8.aarch64.rpm && \
    wget https://rpmfind.net/linux/openmandriva/cooker/repository/aarch64/main/release/chromium-browser-120.0.6099.216-1-omv2490.aarch64.rpm && \
    sudo dnf localinstall nss-mdns-0.14.1-9.el8.aarch64.rpm && \
    sudo dnf localinstall chromium-common-120.0.6099.216-1.el8.aarch64.rpm && \
    sudo dnf localinstall chromedriver-120.0.6099.216-1.el8.aarch64.rpm && \
    sudo dnf localinstall chromium-browser-120.0.6099.216-1-omv2490.aarch64.rpm

WORKDIR /app

#ENV LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/app/usr/bin

RUN chmod a+x /app

COPY build/libs/*.jar live-feed-batch.jar

ENTRYPOINT ["java", "-jar", "/app/live-feed-batch.jar", "--spring.profiles.active=${active_profile}"]
