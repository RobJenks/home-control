FROM adoptopenjdk:11-jre-hotspot-bionic
ARG TARGET_JAR="monitor-agent-hue/target/monitor-agent-hue-master-SNAPSHOT.jar"

EXPOSE 14220/tcp 14221/tcp

COPY ${TARGET_JAR} monitor-agent-hue.jar
ENTRYPOINT ["java", "-jar", "/monitor-agent-hue.jar"]

