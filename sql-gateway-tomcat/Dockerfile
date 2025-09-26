# --- Stage 1: Build WAR file using Maven ---
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml và tải dependencies trước
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

# Copy code và build WAR
COPY src ./src
RUN mvn -q -DskipTests package

# --- Stage 2: Run on Tomcat 10 ---
FROM tomcat:10.1-jdk17

# Xóa các ứng dụng mặc định của Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy WAR build ra thành ROOT.war để chạy ở "/"
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
