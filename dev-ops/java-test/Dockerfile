FROM java:8
WORKDIR /app
ADD *.jar /app/app.jar
COPY run.sh /app/run.sh
EXPOSE {{appServerPort}}
ENTRYPOINT [ "/bin/sh", "/app/run.sh" ]
STOPSIGNAL SIGTERM



