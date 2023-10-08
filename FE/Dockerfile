# Stage 1: For build React app
# Sử dụng node
FROM node:16.16.0-alpine as node

# Khai báo tham số
ARG workdir=.
LABEL description="deploy react app"

# Khái báo workdir trong node.
WORKDIR /app

# Copy project vào trong workdir của node.
COPY ${workdir}/ /app/

# Cài đặt các thư viện node liên quan.
RUN npm install --force

# Chạy lệnh build.
RUN npm run build

# Stage 2: For serving React app
FROM nginx:latest

# Không cần thiết lập workdir lại, vì chúng ta đã nói ở trên.

# Copy folder đã được build vào folder chạy của nginx.
COPY --from=node /app/build/ /usr/share/nginx/html/

# Cài đặt curl cho câu lệnh check HEALTH
RUN apt-get update && apt-get install -y curl

# Kiểm tra trạng thái của container sau khi chạy
HEALTHCHECK --interval=1m --timeout=3s \
  CMD curl -f http://localhost || exit 1

# Thay đổi cổng của nginx từ 80 thành 3000
EXPOSE 3000

CMD ["nginx", "-g", "daemon off;"]
