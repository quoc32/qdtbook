# Chat API Documentation

Đây là tài liệu cho các API chat được triển khai theo yêu cầu.

## API Endpoints

### 1. Kiểm tra xem đã có đoạn chat giữa 2 user chưa

**Endpoint:** `POST /api/chats/check-direct-chat`

**Mô tả:** Kiểm tra xem có tồn tại cuộc trò chuyện trực tiếp giữa hai người dùng hay không.

**Request Body:**
```json
{
    "userId1": 1,
    "userId2": 2
}
```

**Response:**
```json
{
    "exists": true,
    "chatId": 123,
    "message": "Direct chat found"
}
```

**HTTP Status Codes:**
- `200 OK`: Thành công
- `400 Bad Request`: Dữ liệu đầu vào không hợp lệ

---

### 2. Tạo đoạn chat giữa 2 user (hoặc lấy nếu đã tồn tại)

**Endpoint:** `POST /api/chats/find-or-create-direct`

**Mô tả:** Tìm hoặc tạo mới cuộc trò chuyện trực tiếp giữa hai người dùng. Nếu đã tồn tại thì trả về thông tin cuộc trò chuyện hiện có.

**Request Body:**
```json
{
    "userId1": 1,
    "userId2": 2
}
```

**Response:**
```json
{
    "chatId": 123,
    "otherUserId": 2,
    "otherUserName": "Nguyễn Văn B",
    "otherUserAvatar": "/defaults/avatar.png",
    "isOnline": null,
    "createdAt": "2024-10-16T10:30:00Z",
    "lastMessage": null,
    "unreadCount": null
}
```

**HTTP Status Codes:**
- `201 Created`: Tạo thành công hoặc trả về cuộc trò chuyện hiện có
- `400 Bad Request`: Dữ liệu đầu vào không hợp lệ hoặc người dùng không tồn tại

---

### 3. Lấy chat_id của 2 user

**Endpoint:** `POST /api/chats/get-chat-id`

**Mô tả:** Lấy ID của cuộc trò chuyện giữa hai người dùng (chỉ trả về nếu đã tồn tại).

**Request Body:**
```json
{
    "userId1": 1,
    "userId2": 2
}
```

**Response (nếu tìm thấy):**
```json
{
    "chatId": 123,
    "message": "Chat ID found successfully"
}
```

**HTTP Status Codes:**
- `200 OK`: Tìm thấy chat ID
- `404 Not Found`: Không tìm thấy cuộc trò chuyện
- `400 Bad Request`: Dữ liệu đầu vào không hợp lệ

---

## Validation Rules

1. **userId1** và **userId2** phải được cung cấp
2. **userId1** và **userId2** phải khác nhau (không thể tạo chat với chính mình)
3. Cả hai người dùng phải tồn tại trong hệ thống

## Lưu ý quan trọng

1. **Tính duy nhất:** Hệ thống đảm bảo chỉ có một cuộc trò chuyện trực tiếp giữa hai người dùng bất kỳ.

2. **Chat trực tiếp vs Chat nhóm:** 
   - Chat trực tiếp: `is_group = false`, chỉ có 2 thành viên
   - Chat nhóm: `is_group = true`, có thể có nhiều thành viên

3. **Quyền thành viên:** Tất cả thành viên trong chat trực tiếp đều có vai trò `member`.

4. **Thứ tự userId:** API hoạt động với bất kỳ thứ tự nào của userId1 và userId2.

## Cấu trúc Database được sử dụng

### Bảng Chats
- `chat_id`: ID duy nhất của cuộc trò chuyện
- `chat_name`: Tên chat (NULL cho chat trực tiếp)
- `chat_avatar_url`: Avatar của chat (NULL cho chat trực tiếp)
- `is_group`: FALSE cho chat trực tiếp, TRUE cho chat nhóm
- `created_at`: Thời gian tạo

### Bảng Chat_Members
- `chat_id`: Tham chiếu đến bảng Chats
- `user_id`: Tham chiếu đến bảng Users
- `role_in_chat`: Vai trò trong chat (member/admin)

## Ví dụ sử dụng với curl

```bash
# Kiểm tra chat có tồn tại không
curl -X POST http://localhost:8080/api/chats/check-direct-chat \
  -H "Content-Type: application/json" \
  -d '{"userId1": 1, "userId2": 2}'

# Tạo hoặc lấy chat
curl -X POST http://localhost:8080/api/chats/find-or-create-direct \
  -H "Content-Type: application/json" \
  -d '{"userId1": 1, "userId2": 2}'

# Lấy chat ID
curl -X POST http://localhost:8080/api/chats/get-chat-id \
  -H "Content-Type: application/json" \
  -d '{"userId1": 1, "userId2": 2}'
```