# 使用官方 OpenJDK 运行时作为基础镜像
FROM openjdk:17-jdk-slim

# 在镜像中创建一个目录来存放你的应用
RUN mkdir -p /app
# 设置工作目录
WORKDIR /app

# 将构建好的 Jar 包复制到镜像中。请将 `your-project-1.0.0.jar` 替换为你的实际 Jar 包名
COPY target/R024NetWork-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# 指定容器启动时运行的命令
ENTRYPOINT ["java", "-jar", "/app/app.jar"]