# Sử dụng một image base với JDK 17 và MySQL
FROM openjdk:17-jdk-alpine

# Thiết lập thư mục làm việc
WORKDIR /app

# Sao chép file jar của ứng dụng vào container
COPY target/hairsalon-0.0.1-SNAPSHOT.jar app.jar

# Chạy ứng dụng khi container khởi động
CMD ["java", "-jar", "app.jar"]
