FROM adoptopenjdk:11-jre-hotspot-bionic
ARG TARGET_JAR="target/monitor-agent-hue-master-SNAPSHOT.jar"

COPY ${TARGET_JAR} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]

