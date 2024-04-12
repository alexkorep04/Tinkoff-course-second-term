FROM openjdk:21
COPY target/scrapper.jar scrapper.jar
CMD ["java", "-jar", "scrapper.jar"]
