FROM adoptopenjdk:11-jre-hotspot-bionic
ARG TARGET_JAR="monitor-agent-awair/target/monitor-agent-awair-master-SNAPSHOT.jar"

COPY ${TARGET_JAR} monitor-agent-awair.jar
ENTRYPOINT ["java", "-jar", "/monitor-agent-awair.jar"]

