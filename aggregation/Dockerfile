FROM adoptopenjdk:11-jre-hotspot-bionic
ARG TARGET_JAR="aggregation/target/aggregation-master-SNAPSHOT.jar"

COPY ${TARGET_JAR} aggregation.jar
ENTRYPOINT ["java", "-jar", "/aggregation.jar"]

