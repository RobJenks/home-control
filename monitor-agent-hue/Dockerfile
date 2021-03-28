FROM adoptopenjdk:11-jre-hotspot-bionic
ARG TARGET_JAR="monitor-agent-hue/target/monitor-agent-hue-master-SNAPSHOT.jar"

COPY ${TARGET_JAR} monitor-agent-hue.jar
ENTRYPOINT ["java", "-jar", "/monitor-agent-hue.jar"]

