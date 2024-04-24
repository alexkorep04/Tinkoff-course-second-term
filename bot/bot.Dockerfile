FROM openjdk:21
COPY target/bot.jar bot.jar
CMD ["java", "-jar", "bot.jar"]
