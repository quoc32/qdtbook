#  QDTBook - Mạng Xã Hội Trường Đại Học 

**Nền tảng mạng xã hội nội bộ dành riêng cho cộng đồng Trường Đại học Sư phạm Kỹ thuật TP.HCM**

**GitHub Repository**: [https://github.com/quoc32/qdtbook](https://github.com/quoc32/qdtbook)

---

## 📖 Mục Lục

- [Giới Thiệu](#-giới-thiệu)
- [Công Nghệ Sử Dụng](#️-công-nghệ-sử-dụng)
- [Cấu Trúc Thư Mục](#-cấu-trúc-thư-mục)
- [Tính Năng Chi Tiết](#-tính-năng-chi-tiết)
- [Cài Đặt & Chạy Dự Án](#️-cài-đặt--chạy-dự-án)
- [Kiến Thức Học Được](#-kiến-thức-học-được)
- [Tác Giả](#-tác-giả)

---

## 🎯 Giới Thiệu

**QDTBook** là một trang web mạng xã hội được thiết kế dành riêng cho môi trường trường đại học, giúp sinh viên, giảng viên và cán bộ kết nối, chia sẻ thông tin và tương tác với nhau. Hệ thống được xây dựng với kiến trúc hiện đại, tích hợp đầy đủ các tính năng của một mạng xã hội chuyên nghiệp.

### ✨ Điểm Nổi Bật

- **Xác thực đa dạng**: Hỗ trợ đăng ký/đăng nhập thông thường và OAuth2 (Google)
- **Nhắn tin thời gian thực**: Sử dụng WebSocket để chat real-time tương tự như các nền tảng mạng xã hội hiện đại như facebook, instagram, twitter
- **Giao diện hiện đại**: Responsive design với Thymeleaf + Bootstrap
- **Bảo mật cao**: Spring Security với session management
- **Tính năng đặc biệt**: Mạng xã hội này tích hợp 1 trang "Bài viết quan trọng" cho phép thông báo các thông tin quan trọng như thông tin đăng kí học phần, sự kiện quan trọng, lễ tốt nghiệp cho sinh viên, thông báo về các hoạt động của trường,...
- **Hệ thống thông báo**: Nhận thông báo về lời mời kết bạn, bình luận, reaction, tin nhắn mới và các hoạt động liên quan
- **Quản lý Profile**: Trang cá nhân với đầy đủ thông tin, chỉnh sửa profile, upload avatar/cover photo, xem profile người khác
- **Marketplace**: Chợ sinh viên để mua bán đồ cũ
- **Quản lý bạn bè**: Gửi lời mời, chấp nhận, từ chối, chặn
- **Admin Dashboard**: Quản lý người dùng, bài viết, báo cáo vi phạm

---

## 🛠️ Công Nghệ Sử Dụng

### Backend Framework & Core
- **Spring Boot 4.0.0-M2** 
- **Java 17** 
- **Maven** 

### Database & Persistence
- **MySQL** 
- **Spring Data JPA** 
- **Hibernate** 

### Security & Authentication
- **Spring Security** 
- **OAuth2 Client** 
- **BCrypt** 
- **Session-based Auth** 

### Real-time Communication
- **Spring WebSocket** 

### View Layer & Frontend
- **Thymeleaf** 
- **Bootstrap** 
- **HTML5/CSS3/JavaScript** 

### Additional Libraries
- **Lombok** 
- **Spring Mail** - Gửi email (OTP, thông báo)

---

---

## 📁 Cấu Trúc Thư Mục

```
dqtbook_backend/
├── src/main/java/qdt/hcmute/vn/dqtbook_backend/
│   ├── config/              # Cấu hình (Security, WebSocket, CORS)
│   ├── controller/          # REST API Controllers
│   ├── service/             # Business Logic
│   ├── model/               # Entity Classes
│   ├── repository/          # Data Access Layer
│   ├── dto/                 # Data Transfer Objects
│   ├── exception/           # Custom Exceptions
│   ├── enums/               # Enumerations
│   └── view/                # View Controllers (Thymeleaf)
│
├── src/main/resources/
│   ├── application.properties
│   ├── static/              # CSS, JS, Images
│   └── templates/           # Thymeleaf HTML templates
│       ├── index.html       # Trang chủ (Newsfeed)
│       ├── login.html       # Đăng nhập
│       ├── register.html    # Đăng ký
│       ├── user.html        # Trang cá nhân
│       ├── another_user.html # Xem profile người khác
│       ├── friends.html     # Danh sách bạn bè
│       ├── messages.html    # Tin nhắn
│       ├── event.html       # Sự kiện
│       ├── market.html      # Marketplace
│       ├── adminManager.html # Quản trị viên
│       ├── error.html       # Trang lỗi
│       └── fragments/       # Thymeleaf fragments
│
├── pom.xml
└── .env                     # Environment variables
```

---

## 🎨 Tính Năng Chi Tiết

### 1. 🔐 Xác Thực & Phân Quyền
- Đăng ký/Đăng nhập (Email + Password)
- OAuth2 Google Sign-In
- Quên mật khẩu với OTP qua email
- Session-based authentication
- Phân quyền: Student, Special, Admin

### 2. 📝 Quản Lý Bài Viết
- Tạo bài viết với text + media (ảnh/video)
- Bài viết đặc biệt (Important posts)
- Phân quyền hiển thị: Public, Friends, Private
- Chỉnh sửa và xóa bài viết

### 3. 👍 Tương Tác Bài Viết
- **Reactions**: Like, Love, Haha, Wow, Sad, Angry
- **Comments**: Bình luận, chỉnh sửa, xóa
- **Share**: Chia sẻ bài viết với text tùy chỉnh

### 4. 👥 Quản Lý Bạn Bè
- Gửi/Chấp nhận/Từ chối lời mời kết bạn
- Hủy kết bạn, Chặn người dùng
- Gợi ý kết bạn 
- Hiển thị trạng thái online

### 5. 💬 Nhắn Tin Real-time
- Chat 1-1 và nhóm chat
- Read status (đã đọc)
- Gửi ảnh trong chat
- Lịch sử tin nhắn
- Nhắn tin với người lạ

### 6. 🔔 Hệ Thống Thông Báo
- Lời mời kết bạn
- Bình luận, reaction, share
- Bài viết của bạn bè
- Thông báo hệ thống

### 7. 🛒 Marketplace (Chợ Sinh Viên)
- Đăng sản phẩm bán
- Tìm kiếm, lọc theo giá, địa điểm
- Quản lý sản phẩm của mình

### 8. 📊 Admin Dashboard
- Quản lý người dùng (ban/unban)
- Xóa bài viết vi phạm
- Xem báo cáo từ người dùng
- Thống kê user trong hệ thống

---

## ⚙️ Cài Đặt & Chạy Dự Án

### 1. Yêu Cầu Hệ Thống
- Java 17 hoặc cao hơn
- MySQL
- Maven 3.6+
- IDE: IntelliJ IDEA / VS Code

### 2. Clone Dự Án
```bash
git clone https://github.com/quoc32/qdtbook.git
cd qdtbook/backend/dqtbook_backend
```

### 3. Cấu Hình Database
Tạo database MySQL:
```sql
CREATE DATABASE spkt_connect CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 4. Cấu Hình Biến Môi Trường
Tạo file `.env` trong thư mục `backend/dqtbook_backend/`:

```properties
# Database Configuration

DB_HOST=<mysql database url>(ví dụ: jdbc:mysql://localhost:3306/spkt_connect?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC)

DB_USERNAME=<user_name>

DB_PASSWORD=<Mật khẩu database>

MAIL_USERNAME=<email Đăng ký dịch vụ smtp>

MAIL_PASSWORD=<app_word>

GOOGLE_CLIENT_ID=<gg_client_id>

GOOGLE_CLIENT_SECRET=<gg_client_token>
```

### 5. Chạy Ứng Dụng
```bash
mvn clean install
mvn spring-boot:run
```
### 6. Truy Cập Ứng Dụng
- **URL**: http://localhost:8080
- **Login**: Đăng ký tài khoản mới hoặc dùng Google Sign-In

---

## 📚 Kiến Thức Học Được

---
- Xây dựng RESTful API với Spring Boot
- Xác thực & phân quyền bằng Session
- Tương tác cơ sở dữ liệu với JPA / Hibernate
- Realtime communication (WebSocket)
- Tổ chức kiến trúc 3 tầng: Controller – Service – Repository
---

## 👨‍💻 Tác Giả

| Họ Tên | GitHub | MSSV |
|--------|--------|------|
| **Vũ Anh Quốc** | [@quoc32](https://github.com/quoc32) | 23110296 |
| **Trương Quang Điệp** | [@TruongQuangDiep](https://github.com/TruongQuangDiep) | 23110205 |
| **Võ Văn Tú** | [@anhtudayne](https://github.com/anhtudayne) | 23110359 |

---

## 📄 License

Dự án này được phát triển cho mục đích học tập tại Trường Đại học Sư phạm Kỹ thuật TP.HCM.

---
