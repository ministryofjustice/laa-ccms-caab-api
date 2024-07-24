FROM eclipse-temurin:21

# Use a build argument for version
ARG app_version=0.0.1-SNAPSHOT

VOLUME /tmp

COPY caab-service-${app_version}.jar caab-service.jar

EXPOSE 8080
RUN groupadd --system --gid 800 customgroup \
    && useradd --system --uid 800 --gid customgroup --shell /bin/sh customuser
RUN chown customuser:customgroup caab-service.jar
USER 800

ENV TZ=Europe/London
ENV JAVA_TOOL_OPTIONS="-XX:InitialRAMPercentage=50.0 -XX:MaxRAMPercentage=80.0"
CMD java -jar caab-service.jar
