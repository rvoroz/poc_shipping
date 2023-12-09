# carrier-shipping service
This service provides RESTful api endpoints to integrate with carrier services in order to handle shipments

## Prerequisites
- Git
- Maven
- Java
- Docker
- MySql

## Environment Setting
Application uses Spring Profiles which can be configured on file on [application.yml](src/main/resources/application.yml)
 - local
 - dev
 - test 
 - prod

Note: Use -Dspring.profiles.active={PROFILE} as vmargs to configure profile 

## Run Local
Run `mvn spring-boot:run`

## Run with Docker
Go to root folder`cd ..`
Build the image `docker build --tag carrier-shipping .`
and run the container `docker run -it -d --name springboot-propify-shipping -p 8080:8080 carrier-shipping`