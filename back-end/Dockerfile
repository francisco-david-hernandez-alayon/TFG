FROM openjdk:17-jdk-alpine

# Default args
ARG SHARED_CONTAINER_PATH=/app/files
ARG HOST_FILES_PATH=/home/david/workflow-maker/files

ENV SHARED_CONTAINER_PATH=${SHARED_CONTAINER_PATH}
ENV HOST_FILES_PATH=${HOST_FILES_PATH}

# To use Docker commands in the container
RUN apk add --no-cache docker-cli

EXPOSE 8080

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]