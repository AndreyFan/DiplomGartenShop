# Use a base image with Java installed
FROM openjdk:22

# Creator
MAINTAINER alex.com

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container at /app
COPY target/*.jar onlineshop.jar

# Specify the command to run your application
#CMD ["java", "-jar", "app.jar"]
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "onlineshop.jar"]