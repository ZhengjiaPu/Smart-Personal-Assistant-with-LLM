# 第一阶段：使用 Maven 构建项目
FROM maven:3.8.1-openjdk-17 AS builder

# 设置工作目录
WORKDIR /app

# 复制 Maven 配置文件和源代码
COPY pom.xml ./
COPY src ./src

# 使用 Maven 构建项目，并生成可执行的 JAR 文件
RUN mvn clean package -DskipTests

# 第二阶段：使用 JDK 运行 Spring Boot 应用
FROM openjdk:17-jdk-slim

# 设置工作目录
WORKDIR /app

# 从第一阶段复制构建好的 JAR 文件到运行环境
COPY --from=builder /app/target/smartassistant-backend-0.0.1-SNAPSHOT.jar app.jar

# 暴露 Spring Boot 应用的端口
EXPOSE 8101

# 设置启动命令，运行 Spring Boot 应用，并指定 prod 配置文件
CMD ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
