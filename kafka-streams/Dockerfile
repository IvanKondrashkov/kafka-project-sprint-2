FROM amazoncorretto:21-alpine-jdk
RUN apk add --no-cache libstdc++
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
EXPOSE 9090