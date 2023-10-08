# Sử dụng một hình ảnh cơ sở Java
FROM openjdk:17-jdk-alpine

# Tạo thư mục /app trong hệ thống tệp Docker và đặt nó làm thư mục làm việc mặc định
WORKDIR /app

# Sao chép tệp JAR của ứng dụng Spring Boot vào thư mục /app trong hệ thống tệp Docker
COPY target/server-0.0.1-SNAPSHOT.jar ./app.jar

# Đặt biến môi trường SPRING_PROFILES_ACTIVE (nếu cần)
# ENV SPRING_PROFILES_ACTIVE=production

# Expose cổng mạng cho ứng dụng Spring Boot (thay đổi số cổng cần thiết)
# khai báo rằng container sẽ lắng nghe kết nối đến cổng 8080
EXPOSE 8080

# Khởi chạy ứng dụng Spring Boot khi container được khởi động
CMD ["java", "-jar", "app.jar"]
