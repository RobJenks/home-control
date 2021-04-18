FROM adoptopenjdk:11-jre-hotspot-bionic
ARG TARGET_JAR="target/monitor-agent-awair-master-SNAPSHOT.jar"

EXPOSE 14210/tcp 14211/tcp

COPY ${TARGET_JAR} monitor-agent-awair.jar
ENTRYPOINT ["java", "-jar", "/monitor-agent-awair.jar"]

