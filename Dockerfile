# ---- build stage ----
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -B dependency:go-offline
COPY src ./src
RUN mvn -q -B package -DskipTests

# ---- runtime stage: Tomcat 10.1 ----
FROM tomcat:10.1-jdk17-temurin
RUN rm -rf /usr/local/tomcat/webapps/* && mkdir -p /data/uploads
COPY --from=build /app/target/ROOT.war /usr/local/tomcat/webapps/ROOT.war
COPY deploy/start.sh /start.sh
RUN chmod +x /start.sh
ENV UPLOAD_DIR=/data/uploads
EXPOSE 8080
CMD ["/start.sh"]
