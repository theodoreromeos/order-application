FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

#if .war is needed
#COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war
#RUN sed -i 's/port="8080"/port="9090"/' /usr/local/tomcat/conf/server.xml