# =============================================
# 疲劳监测系统后端Dockerfile
# 基于OpenJDK 11构建Spring Boot应用
# =============================================

# 第一阶段：构建阶段
FROM maven:3.8.6-openjdk-11-slim AS builder

# 设置工作目录
WORKDIR /app

# 复制pom.xml并下载依赖（利用Docker缓存）
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 复制源代码
COPY src ./src

# 构建应用
RUN mvn clean package -DskipTests

# 第二阶段：运行阶段
FROM openjdk:11-jre-slim

# 设置工作目录
WORKDIR /app

# 从构建阶段复制jar包
COPY --from=builder /app/target/*.jar app.jar

# 创建日志目录
RUN mkdir -p /app/logs

# 设置时区为东八区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 暴露端口
EXPOSE 8080

# 设置JVM参数
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

# 启动应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar"]
