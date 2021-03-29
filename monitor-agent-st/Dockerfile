FROM adoptopenjdk:11-jre-hotspot-bionic
ARG TARGET_JAR="monitor-agent-st/target/monitor-agent-st-master-SNAPSHOT.jar"

EXPOSE 14230/tcp 14231/tcp

COPY ${TARGET_JAR} monitor-agent-st.jar
ENTRYPOINT ["java", "-jar", "/monitor-agent-st.jar"]

