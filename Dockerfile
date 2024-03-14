FROM openjdk:11
WORKDIR /app
COPY ./target/SkiStationProject-0.0.1-SNAPSHOT.jar /app
EXPOSE 8080
CMD ["java", "-jar", "SkiStationProject-0.0.1-SNAPSHOT.jar"]