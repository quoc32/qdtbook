# Message API Documentation

Đây là tài liệu cho các API message trong hệ thống chat.

## API Endpoints

### 1. Gửi tin nhắn mới

**Endpoint:** `POST /api/messages`

**Mô tả:** Gửi tin nhắn mới vào một cuộc trò chuyện.

**Request Body:**
```json
{
    "chat_id": 1,
    "sender_id": 1,
    "content": "Hello, this is a message!",
    "media_url": "https://example.com/image.jpg"
}
```

**Response (201 Created):**
```json
{
    "message_id": 123,
    "chat_id": 1,
    "sender_id": 1,
    "sender_full_name": "Nguyễn Văn A",
    "sender_avatar_url": "/defaults/avatar.png",
    "content": "Hello, this is a message!",
    "media_url": "https://example.com/image.jpg",
    "created_at": "2024-10-16T10:30:00Z",
    "read_by": []
}
```

---

### 2. Lấy tin nhắn của một cuộc trò chuyện

**Endpoint:** `GET /api/messages/chat/{chatId}?userId={userId}`

**Mô tả:** Lấy tất cả tin nhắn trong một cuộc trò chuyện cụ thể.

**Parameters:**
- `chatId` (path): ID của cuộc trò chuyện
- `userId` (query): ID của người dùng yêu cầu

**Response (200 OK):**
```json
[
    {
        "message_id": 123,
        "chat_id": 1,
        "sender_id": 1,
        "sender_full_name": "Nguyễn Văn A",
        "sender_avatar_url": "/defaults/avatar.png",
        "content": "Hello!",
        "media_url": null,
        "created_at": "2024-10-16T10:30:00Z",
        "read_by": [
            {
                "user_id": 2,
                "user_full_name": "Nguyễn Văn B",
                "read_at": "2024-10-16T10:31:00Z"
            }
        ]
    }
]
```

---

### 3. Lấy tin nhắn cụ thể theo ID

**Endpoint:** `GET /api/messages/{messageId}?userId={userId}`

**Mô tả:** Lấy thông tin chi tiết của một tin nhắn cụ thể.

**Parameters:**
- `messageId` (path): ID của tin nhắn
- `userId` (query): ID của người dùng yêu cầu

**Response (200 OK):**
```json
{
    "message_id": 123,
    "chat_id": 1,
    "sender_id": 1,
    "sender_full_name": "Nguyễn Văn A",
    "sender_avatar_url": "/defaults/avatar.png",
    "content": "Hello!",
    "media_url": null,
    "created_at": "2024-10-16T10:30:00Z",
    "read_by": [...]
}
```

---

### 4. Đánh dấu tin nhắn đã đọc

**Endpoint:** `POST /api/messages/{messageId}/read?userId={userId}`

**Mô tả:** Đánh dấu một tin nhắn cụ thể là đã đọc bởi người dùng.

**Parameters:**
- `messageId` (path): ID của tin nhắn
- `userId` (query): ID của người dùng

**Response (200 OK):**
```json
{
    "status": "success"
}
```

---

### 5. Đánh dấu tất cả tin nhắn đã đọc

**Endpoint:** `POST /api/messages/chat/{chatId}/read-all?userId={userId}`

**Mô tả:** Đánh dấu tất cả tin nhắn trong cuộc trò chuyện là đã đọc bởi người dùng.

**Parameters:**
- `chatId` (path): ID của cuộc trò chuyện
- `userId` (query): ID của người dùng

**Response (200 OK):**
```json
{
    "status": "success"
}
```

---

### 6. Xóa tin nhắn

**Endpoint:** `DELETE /api/messages/{messageId}?userId={userId}`

**Mô tả:** Xóa một tin nhắn. Chỉ người gửi tin nhắn mới có thể xóa.

**Parameters:**
- `messageId` (path): ID của tin nhắn
- `userId` (query): ID của người dùng (phải là người gửi)

**Response (204 No Content):**
```
(Empty response body)
```

---

## Validation Rules

### Gửi tin nhắn:
1. `chat_id` và `sender_id` là bắt buộc
2. Ít nhất một trong `content` hoặc `media_url` phải có giá trị
3. Người gửi phải là thành viên của cuộc trò chuyện
4. Cuộc trò chuyện phải tồn tại

### Lấy tin nhắn:
1. Người dùng phải là thành viên của cuộc trò chuyện
2. Cuộc trò chuyện/tin nhắn phải tồn tại

### Đánh dấu đã đọc:
1. Người dùng phải là thành viên của cuộc trò chuyện
2. Tin nhắn phải tồn tại
3. Người dùng không thể đánh dấu đã đọc tin nhắn của chính mình

### Xóa tin nhắn:
1. Chỉ người gửi tin nhắn mới có thể xóa
2. Tin nhắn phải tồn tại

## Error Codes

| Status Code | Mô tả |
|-------------|--------|
| 200 | Thành công |
| 201 | Tạo thành công |
| 204 | Xóa thành công |
| 400 | Dữ liệu đầu vào không hợp lệ |
| 401 | Không có quyền truy cập |
| 404 | Không tìm thấy |
| 500 | Lỗi server |

## Lưu ý quan trọng

1. **Quyền truy cập:** Chỉ các thành viên của cuộc trò chuyện mới có thể xem và gửi tin nhắn.

2. **Trạng thái đã đọc:** 
   - Người gửi không cần đánh dấu đã đọc tin nhắn của chính mình
   - Trạng thái đã đọc được theo dõi cho từng người dùng riêng biệt

3. **Media URL:** 
   - Hệ thống chỉ lưu URL của media, không lưu trữ file
   - Media URL có thể trỏ đến file trong hệ thống lưu trữ khác

4. **Xóa tin nhắn:**
   - Chỉ xóa soft delete (có thể phục hồi)
   - Chỉ người gửi mới có thể xóa tin nhắn của mình

5. **Timestamp:** Tất cả timestamp sử dụng UTC timezone.

## Ví dụ workflow cơ bản

```bash
# 1. Tạo cuộc trò chuyện (nếu chưa có)
curl -X POST http://localhost:8080/api/chats/find-or-create-direct \
  -H "Content-Type: application/json" \
  -d '{"userId1": 1, "userId2": 2}'

# 2. Gửi tin nhắn
curl -X POST http://localhost:8080/api/messages \
  -H "Content-Type: application/json" \
  -d '{"chat_id": 1, "sender_id": 1, "content": "Hello!"}'

# 3. Lấy tin nhắn của cuộc trò chuyện
curl -X GET "http://localhost:8080/api/messages/chat/1?userId=2"

# 4. Đánh dấu đã đọc
curl -X POST "http://localhost:8080/api/messages/123/read?userId=2"

# 5. Xóa tin nhắn (chỉ người gửi)
curl -X DELETE "http://localhost:8080/api/messages/123?userId=1"
```