FROM openjdk:8-jdk-alpine

ENV LANG zh_CN.UTF-8

RUN echo -e "https://mirror.tuna.tsinghua.edu.cn/alpine/v3.4/main\n\
https://mirror.tuna.tsinghua.edu.cn/alpine/v3.4/community" > /etc/apk/repositories

RUN apk --update add curl bash ttf-dejavu && \
      rm -rf /var/cache/apk/*

COPY permission-service/target/lesscode-permission-service.jar /data/app/lesscode-permission/lesscode-permission.jar
COPY bin /data/app/lesscode-permission/bin

WORKDIR /data/app/lesscode-permission/bin

RUN sed -i 's/bash/sh/g' *.sh \
    && echo -e "\ntail -f /dev/null" >> start.sh \
    && cat start.sh

EXPOSE 10670

CMD ["./start.sh"]