FROM openjdk:17-jdk

WORKDIR /app

ENV active_profile=prod
ENV TZ=Asia/Seoul

RUN apt-get update && \
    apt-get install -y tzdata && \
    ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && \
    echo $TZ > /etc/timezone

RUN chmod a+x /app

COPY build/libs/*.jar live-feed-views-batch.jar

ENTRYPOINT ["java", "-jar", "/app/live-feed-views-batch.jar", "--spring.profiles.active=${active_profile}"]