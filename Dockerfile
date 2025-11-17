FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY /target/testBankingSystem-0.0.1-SNAPSHOT.jar /app/bank.jar
ENTRYPOINT ["java", "-jar", "/app/bank.jar"]