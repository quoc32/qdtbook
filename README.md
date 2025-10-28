# Trang mạng xã hội QDTBook – Spring Boot Project
## Giới thiệu
Đây là một trang web mạng xã hội nội bộ dành riêng cho cộng đồng HCMUTE được xây dựng bằng Spring Boot.
Người dùng có thể đăng ký, đăng nhập, tạo bài viết, thích, bình luận, kết bạn và trò chuyện thời gian thực.
Dự án được thực hiện nhằm mục đích học tập, tìm hiểu về kiến trúc MVC, RESTful API, và Spring Security.
## Công nghệ sử dụng
### Backend:
Java 17
Spring Boot (Web, Security, Data JPA, Validation)
Hibernate / JPA
Cơ sở dữ liệu MySQL
Lombok
WebSocket
### Frontend:
Thymeleaf
Bootstrap
### Công cụ phát triển:
Maven
IntelliJ IDEA / VS Code
## Cấu trúc thư mục chính (src)
<img width="353" height="422" alt="image" src="https://github.com/user-attachments/assets/d13e7ba7-2ed1-433d-957a-63d4c980e6e8" />

## Cách chạy dự án
### 1. Clone dự án
git clone https://github.com/quoc32/qdtbook.git
cd backend/qdtbook_backend
### 2. Cấu hình database và biến môi trường
Thêm file .env (cùng cấp thư mục với file pom.xml) bao gồm các thông tin như sau:

DB_HOST=<mysql database url>(ví dụ: jdbc:mysql://localhost:3306/spkt_connect?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC)

DB_USERNAME=<user_name>

DB_PASSWORD=<Mật khẩu database>

MAIL_USERNAME=<email Đăng ký dịch vụ smtp>

MAIL_PASSWORD=<app_word>

GOOGLE_CLIENT_ID=<gg_client_id>

GOOGLE_CLIENT_SECRET=<gg_client_token>

### 3. Chạy ứng dụng
mvn spring-boot:run

## Tính năng chính

✅ Đăng ký / Đăng nhập / Xác thực OTP / JSESSIONID

🧑‍🤝‍🧑 Thêm, xóa, gửi lời mời bạn bè

📝 Đăng bài viết, bài thông báo đặc biệt, chỉnh sửa, xóa,

❤️ Thích / Bình luận bài viết

💬 Chat real-time bằng WebSocket

🔒 Phân quyền người dùng (Admin, Special, Student)

## Kiến thức học được

Xây dựng RESTful API với Spring Boot

Xác thực & phân quyền bằng Session

Tương tác cơ sở dữ liệu với JPA / Hibernate

Realtime communication (WebSocket)

Tổ chức kiến trúc 3 tầng: Controller – Service – Repository

 ## Tác giả

 | Họ Tên | Tài khoản Github | MSSV |
|-------|-------|-------|
| Vũ Anh Quốc | https://github.com/quoc32 | 23110296 |
| Trương Quang Điệp | https://github.com/TruongQuangDiep | 23110205 |
| Võ Văn Tú | https://github.com/anhtudayne | 23110359 |

