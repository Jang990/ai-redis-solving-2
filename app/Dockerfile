# 다중 인스턴스 스테이지용 (cluster profile). 평시엔 호스트 bootRun — 이 파일 수정 불요.
FROM gradle:8.10-jdk21 AS build
WORKDIR /src
COPY . .
RUN gradle bootJar --no-daemon -x test

FROM eclipse-temurin:21-jre
COPY --from=build /src/build/libs/*.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
