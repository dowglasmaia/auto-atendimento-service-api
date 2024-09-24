# Build
FROM maven:3.9.7-amazoncorretto-21 AS build
WORKDIR /api
COPY pom.xml .
RUN mvn dependency:resolve
COPY src ./src
RUN mvn clean install -DskipTests -Dsonar.skip=true

# Runtime
FROM amazoncorretto:21-alpine3.17
LABEL MAINTAINER="DOWGLAS MAIA"
ENV PORT=8099
WORKDIR /usr/src/app
RUN rm -f /etc/localtime && ln -s /usr/share/zoneinfo/America/Sao_Paulo /etc/localtime
COPY --from=build /api/target/*.jar /usr/src/app/maia-bot-service.jar

ENTRYPOINT ["java" , "-Dfile.encoding=UTF-8", "-jar", "/usr/src/app/maia-bot-service.jar", "--server.port=${PORT}"]

EXPOSE ${PORT}
