FROM eclipse-temurin:20-jdk

WORKDIR /app

COPY app/. .

RUN ./gradlew --no-daemon dependencies

RUN ./gradlew --no-daemon build

ENV JAVA_OPTS "-Xmx512M -Xms512M -spring.profiles.active=prod"

CMD java -Dspring.profiles.active=prod -jar build/libs/app-0.0.1-SNAPSHOT.jar
