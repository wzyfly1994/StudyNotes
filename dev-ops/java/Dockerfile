FROM java:8
WORKDIR /app
ADD *.jar /app/app.jar
COPY run.sh /app/run.sh
# 指定容器内的时区
RUN echo "Asia/Shanghai" > /etc/timezone
ENTRYPOINT [ "/bin/sh", "/app/run.sh" ]



