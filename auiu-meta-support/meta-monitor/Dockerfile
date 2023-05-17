FROM evina/jdk17:17.0.1

RUN mkdir -p /meta-monitor

WORKDIR /meta-monitor

ARG JAR_FILE=target/meta-monitor.jar

COPY ${JAR_FILE} app.jar

EXPOSE 21000

ENV TZ=Asia/Shanghai JAVA_OPTS="-Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom"

CMD sleep 60; java $JAVA_OPTS -jar app.jar
