FROM adoptopenjdk:11-jre-hotspot-bionic
ARG TARGET_JAR="aggregation/target/aggregation-master-SNAPSHOT.jar"

EXPOSE 14200/tcp 14201/tcp

COPY ${TARGET_JAR} aggregation.jar
ENTRYPOINT ["java", "-jar", "/aggregation.jar"]
