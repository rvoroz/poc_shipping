# First stage: complete build environment
FROM maven:3-eclipse-temurin-11 AS builder

# add pom.xml and source code
ADD ./pom.xml pom.xml
ADD ./.mvn .mvn/
ADD ./carrier-shipping carrier-shipping/
ADD ./api-automation api-automation/

# package jar 
RUN mvn clean package

# Second stage: minimal runtime environment
FROM openjdk:8-jre-alpine

# copy jar from the first stage
COPY --from=builder /carrier-shipping/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-Dspring.profiles.active=dev", "-jar", "app.jar"]
