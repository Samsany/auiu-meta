FROM ubuntu:latest
MAINTAINER dries samphsanie@gmail.com

#更新软件源
RUN apt-get update
#安装字体支持
RUN apt-get install fontconfig -y
#清理缓存
RUN apt-get clean

#在windows环境下构建，将/usr/share/zoneinfo/Asia/Shanghai拷贝到本地目录
# COPY ./Shanghai /etc/localtime

RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \&& echo 'Asia/Shanghai' >/etc/timezone

#setup language 解决中文乱码
#设置中文支持
ENV LANG C.UTF-8
#解压jdk并删除lib目录下的源码
COPY ./jre-17 /usr/local/jre17
#授执行权限
RUN chmod -R 750 /usr/local/jre17/bin

#setup java env
ENV JAVA_HOME=/usr/local/jre17
ENV PATH=$PATH:$JAVA_HOME/bin
ENV CALSSPATH=$JAVA_HOME/jre/lib/rt.jar:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
