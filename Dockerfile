FROM openjdk:11-jdk-slim

WORKDIR /app

# JAR 파일 복사 (경로는 실제 빌드 위치에 맞게 조정)
COPY target/*.jar app.jar

# 파일 저장 디렉토리 생성
RUN mkdir -p /app/files/product && \
    chmod -R 777 /app/files

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]