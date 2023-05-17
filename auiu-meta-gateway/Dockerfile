#FROM evina/jdk17:17.0.7
#
#MAINTAINER samphsanie@gmail.com
#
#ENV JVM_OPTS="\
#-server \
#-Xmx256m \
#-Xms256m \
#-Xmn128m \
#-XX:SurvivorRatio=8 \
#-XX:MetaspaceSize=128m \
#-XX:MaxMetaspaceSize=320m \
#-XX:+UseParallelGC \
#-XX:ParallelGCThreads=4 \
#-XX:+UseParallelOldGC \
#-XX:+UseAdaptiveSizePolicy \
#-XX:+PrintGCDetails \
#-XX:+PrintTenuringDistribution \
#-XX:+PrintGCTimeStamps \
#-XX:+HeapDumpOnOutOfMemoryError \
#-XX:HeapDumpPath=/ \
#-Xloggc:/gc.log \
#-XX:+UseGCLogFileRotation \
#-XX:NumberOfGCLogFiles=5 \
#-XX:GCLogFileSize=10M"
#
#RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
#
#RUN mkdir -p /meta-gateway
#
#RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \&& echo 'Asia/Shanghai' >/etc/timezone
#
#WORKDIR /meta-gateway
#
## /tmp 目录作为容器数据卷目录，SpringBoot内嵌Tomcat容器默认使用/tmp作为工作目录，任何向 /tmp 中写入的信息不会记录进容器存储层，从而保证容器存储层的无状态化
## 在宿主机的/var/lib/docker目录下创建一个临时文件并把它链接到容器中的/tmp目录
#VOLUME /tmp
#
#EXPOSE 88
#
#ADD ./target/auiu-meta-gateway.jar ./app.jar
#
#ENTRYPOINT java ${JVM_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /app.jar
#
#CMD sleep 1;java -Dfile.encoding=utf-8 -Djava.security.egd=file:/dev/./urandom -jar app.jar

FROM evina/jdk17:17.0.1

RUN mkdir -p /meta-gateway

WORKDIR /meta-gateway

ARG JAR_FILE=target/auiu-meta-gateway.jar

COPY ${JAR_FILE} app.jar

EXPOSE 88

ENV TZ=Asia/Shanghai JAVA_OPTS="-Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom"

CMD sleep 60; java $JAVA_OPTS -jar app.jar
