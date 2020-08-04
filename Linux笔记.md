![title](./images/title.jpg)

##  Linux

***

### Docker命令

```
DockerToolbox
docker默认账号密码
docker  tcuser ---ssh方式进入
sudu -i  ---进入root

docker images---查看镜像

docker-machine  ls --查看machine所有列表

# 开启
docker-machine start default
 
# 关闭
docker-machine stop default

history |grep docker  --查看docker  历史操作

docker pull jenkins/jenkins:latest --安装最近版本jenkins

docker-machine ssh default---windos 连接docker

docker exec -it  name bash ---进入jenkins docker win7

docker exec -it  name /bin/bash  win10

root进入

docker exec -it -u root  id  bash

docker run -d -p 8080:8080 -p 50000:50000 -v jenkins_home:/var/jenkins_home --name jenkins  jenkins/jenkins:latest  --启动

docker logs -f --tail=200 jenkins  --日志

docker volume ls
```

### Center os 7

```
yum -y install vim*   ---安装vim 
```

