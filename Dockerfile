# Sử dụng một image base với JDK 17
FROM openjdk:17-jdk-alpine

# Thiết lập thư mục làm việc
WORKDIR /app

# Sao chép file JAR của Axon Server vào container từ đường dẫn khác
COPY target/axonserver.jar .

# Chạy Axon Server khi container khởi động
CMD ["java", "-jar", "axonserver.jar"]
