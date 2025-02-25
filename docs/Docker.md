
```yaml
FROM maven:3.9.7-eclipse-temurin-22 as builder
WORKDIR /app
COPY . /app/.
RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip=true

FROM eclipse-temurin:22-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar /app/*.jar

  #EXPOSE 8181
  #ENTRYPOINT ["java", "-jar", "/app/*.jar"]
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "/app/*.jar"]
```

