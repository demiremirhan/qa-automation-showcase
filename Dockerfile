
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY common/pom.xml common/
COPY api-tests/pom.xml api-tests/
RUN mvn dependency:go-offline -B
COPY common/src common/src
COPY api-tests/src api-tests/src
RUN mvn -pl api-tests -am test


FROM eclipse-temurin:21-jre-alpine AS results
WORKDIR /app
COPY --from=build /app/api-tests/target/allure-results ./allure-results-baked
CMD ["sh", "-c", "cp -r /app/allure-results-baked/* /app/allure-results/ && echo 'Results copied to mounted volume'"]