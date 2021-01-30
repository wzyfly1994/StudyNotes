#!/bin/sh

# 修改时间
cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

# 启动java应用
exec java -Xms512m -Xmx512m -XX:+UseConcMarkSweepGC -jar app.jar --service.registry.expose-host=192.168.4.202 --service.registry.consul-host=192.168.4.202
