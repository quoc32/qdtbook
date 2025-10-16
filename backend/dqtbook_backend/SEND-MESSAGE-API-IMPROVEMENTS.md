# Send Message API - Kiểm tra và Cải tiến

## Những thay đổi đã thực hiện:

### 1. **Cải tiến MessageController.sendMessage()**

**Trước khi cải tiến:**
```java
@PostMapping
public ResponseEntity<?> sendMessage(@RequestBody MessageRequestDTO request) {
    return messageService.sendMessage(request)
            .map(message -> ResponseEntity.status(HttpStatus.CREATED).body(message))
            .orElse(ResponseEntity.badRequest().build());
}
```

**Sau khi cải tiến:**
- ✅ Thêm validation đầy đủ cho request body
- ✅ Kiểm tra `chat_id` và `sender_id` không null
- ✅ Validation ít nhất một trong `content` hoặc `media_url` phải có giá trị
- ✅ Trả về error message rõ ràng thay vì empty response
- ✅ Thêm import `java.util.Map` cho error responses

### 2. **Validation Rules được implement:**

| Validation | Trước | Sau | Error Message |
|------------|-------|-----|---------------|
| Request body null | ❌ | ✅ | "Request body is required" |
| chat_id null | ❌ | ✅ | "chat_id is required" |
| sender_id null | ❌ | ✅ | "sender_id is required" |
| content & media_url đều empty/null | ❌ | ✅ | "Either content or media_url must be provided" |
| Chat không tồn tại | ✅ (service) | ✅ | "Failed to send message..." |
| User không tồn tại | ✅ (service) | ✅ | "Failed to send message..." |
| User không phải member chat | ✅ (service) | ✅ | "Failed to send message..." |

### 3. **Test Cases được bổ sung:**

#### **Positive Test Cases:**
- ✅ Send text message only
- ✅ Send media message only  
- ✅ Send both text and media
- ✅ Send with empty string content but valid media

#### **Validation Error Test Cases (400 Bad Request):**
- ✅ Missing chat_id
- ✅ Null chat_id
- ✅ Missing sender_id
- ✅ Null sender_id
- ✅ Both content and media_url null
- ✅ Both content and media_url empty strings
- ✅ Content with only whitespace
- ✅ Empty request body

#### **Business Logic Error Test Cases (400 Bad Request):**
- ✅ Invalid chat_id (chat không tồn tại)
- ✅ Invalid sender_id (user không tồn tại)
- ✅ Sender không phải member của chat

## Response Format:

### **Success Response (201 Created):**
```json
{
    "message_id": 123,
    "chat_id": 1,
    "sender_id": 1,
    "sender_full_name": "Nguyễn Văn A",
    "sender_avatar_url": "/defaults/avatar.png",
    "content": "Hello!",
    "media_url": "https://example.com/image.jpg",
    "created_at": "2024-10-16T10:30:00Z",
    "read_by": [
        {
            "user_id": 1,
            "user_full_name": "Nguyễn Văn A",
            "read_at": "2024-10-16T10:30:00Z"
        }
    ]
}
```

### **Error Response (400 Bad Request):**
```json
{
    "error": "chat_id is required"
}
```

## Logic trong MessageService:

### **Validation Flow:**
1. ✅ Kiểm tra chat tồn tại
2. ✅ Kiểm tra user tồn tại  
3. ✅ Kiểm tra user là member của chat
4. ✅ Tạo message và lưu vào database
5. ✅ Tự động mark message đã đọc cho sender
6. ✅ Trả về MessageResponseDTO với thông tin đầy đủ

### **Auto-behaviors:**
- Message được tự động mark là "đã đọc" bởi người gửi
- CreatedAt được set là Instant.now()
- Response bao gồm thông tin sender (full_name, avatar_url)
- Response bao gồm danh sách read_by users

## Cách test:

### **Setup trước khi test:**
```http
# 1. Tạo chat giữa 2 users
POST http://localhost:8080/api/chats/find-or-create-direct
{
    "userId1": 1,
    "userId2": 2
}
```

### **Test các scenarios:**
1. **Happy path:** Gửi tin nhắn thành công
2. **Validation errors:** Test tất cả validation rules
3. **Business errors:** Test với data không hợp lệ
4. **Edge cases:** Empty strings, whitespace, null values

## Lưu ý quan trọng:

1. **Content vs Media:** Ít nhất một trong hai phải có giá trị
2. **Auto-read:** Sender tự động được mark đã đọc message
3. **Member check:** Chỉ members của chat mới gửi được message
4. **Error handling:** Luôn trả về message lỗi rõ ràng
5. **Transaction:** Method được đánh dấu @Transactional để đảm bảo data consistency