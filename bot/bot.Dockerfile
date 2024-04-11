FROM openjdk:21
WORKDIR application
COPY . /application
CMD ["java", "-jar", "target/bot.jar"]
