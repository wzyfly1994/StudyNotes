#!/bin/sh

# 修改时间
cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

# 启动java应用。在docker容器中通过 ps -ef|grep java 查看是否生效
exec java {{jvmOptions}} -jar app.jar {{programArguments}}

