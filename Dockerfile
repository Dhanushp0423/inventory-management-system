# ---- Stage 1: build the JAR (runs the unit tests) ----
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B -q dependency:go-offline
COPY src ./src
RUN mvn -B -q clean package

# ---- Stage 2: small runtime image ----
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/library-inventory-cli.jar app.jar
# Data lives on a volume so it survives container restarts
ENV LIBRARY_DATA_FILE=/data/books.csv
VOLUME /data
# The app is interactive: run with  docker run -it -v library-data:/data <image>
ENTRYPOINT ["java", "-jar", "app.jar"]
