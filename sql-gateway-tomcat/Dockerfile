# syntax=docker/dockerfile:1

###############
# Build stage #
###############
FROM maven:3.9.8-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -DskipTests=true dependency:go-offline
COPY src ./src
RUN mvn -q -DskipTests=true package
# => tạo /app/target/sql-gateway.war

#################
# Runtime stage #
#################
FROM tomcat:10.1-jre17-temurin

# dọn webapps default
RUN rm -rf /usr/local/tomcat/webapps/*

# deploy làm ROOT để vào "/" cho gọn
COPY --from=build /app/target/sql-gateway.war /usr/local/tomcat/webapps/ROOT.war

WORKDIR /usr/local/tomcat
RUN mkdir -p /usr/local/tomcat/data
ENV JAVA_OPTS="-Xms128m -Xmx512m"

# Local sẽ map 8080; Render sẽ dùng $PORT
EXPOSE 8080

# Quan trọng: đổi cổng Tomcat theo $PORT nếu có (Render); nếu không thì 8080 (local)
CMD bash -lc 'sed -ri "s/port=\"8080\"/port=\"${PORT:-8080}\"/" conf/server.xml && catalina.sh run'
