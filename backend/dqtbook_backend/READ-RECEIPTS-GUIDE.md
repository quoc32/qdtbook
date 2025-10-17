# Read Receipts (Đã xem tin nhắn) - Hướng dẫn

## ✅ Đã hoàn thành

Tính năng **Read Receipts** (đánh dấu đã xem tin nhắn) đã được implement vào `messages.html`.

## 🔄 Luồng hoạt động

### **Kịch bản: User B mở chat với User A**

```
User B                                    Server                                    User A
  │                                         │                                         │
  │ 1. Mở chat với User A                   │                                         │
  │    openChat(userA_id)                   │                                         │
  │                                         │                                         │
  │ 2. Load lịch sử tin nhắn                │                                         │
  │    GET /api/messages/chat/{chatId}      │                                         │
  ├────────────────────────────────────────>│                                         │
  │<────────────────────────────────────────┤                                         │
  │                                         │                                         │
  │ 3. Hiển thị tin nhắn lên màn hình       │                                         │
  │    displayRealMessages(messages)        │                                         │
  │                                         │                                         │
  │ 4. Subscribe WebSocket topics           │                                         │
  │    - /topic/chat.{chatId}               │                                         │
  │    - /topic/chat.{chatId}.readStatus    │                                         │
  │                                         │                                         │
  │ 5. Tự động đánh dấu đã đọc              │                                         │
  │    markAllVisibleMessagesAsRead()       │                                         │
  │    (chỉ tin nhắn của User A)            │                                         │
  │                                         │                                         │
  │ 6. Gửi read status qua WebSocket        │                                         │
  │    SEND /app/chat.readStatus            │                                         │
  │    { messageId, userId, chatId }        │                                         │
  ├────────────────────────────────────────>│                                         │
  │                                         │                                         │
  │                                         │ 7. Xác thực userId                      │
  │                                         │ 8. Lưu vào database                     │
  │                                         │    INSERT message_read_status           │
  │                                         │                                         │
  │                                         │ 9. Broadcast read status                │
  │                                         │    /topic/chat.{chatId}.readStatus      │
  │<────────────────────────────────────────┼────────────────────────────────────────>│
  │                                         │                                         │
  │                                         │                  10. Nhận read status   │
  │                                         │                      updateMessageReadStatus()│
  │                                         │                      Hiển thị ✓✓ màu đen│
```

## 📝 Code đã implement

### **1. Biến quản lý subscription**
```javascript
let currentReadStatusSubscription = null;  // Subscription cho read status
```

### **2. Subscribe read status topic**
```javascript
function subscribeToChat(chatId) {
    // ... subscribe tin nhắn ...
    
    // Subscribe vào topic read status
    const readStatusTopic = '/topic/chat.' + chatId + '.readStatus';
    currentReadStatusSubscription = stompClient.subscribe(readStatusTopic, function(message) {
        const readStatus = JSON.parse(message.body);
        updateMessageReadStatus(readStatus);
    });
}
```

### **3. Tự động đánh dấu đã đọc khi nhận tin nhắn mới**
```javascript
currentSubscription = stompClient.subscribe(messageTopic, function(message) {
    const messageData = JSON.parse(message.body);
    displayReceivedMessage(messageData);
    
    // Tự động đánh dấu đã đọc nếu không phải tin nhắn của mình
    const currentUserId = auth.user_id;
    if (messageData.senderId !== currentUserId) {
        setTimeout(() => {
            markMessageAsRead(messageData.id, chatId);
        }, 500);
    }
});
```

### **4. Gửi read status qua WebSocket**
```javascript
function markMessageAsRead(messageId, chatId) {
    const readStatusPayload = {
        chatId: chatId,
        userId: currentUserId,
        messageId: messageId
    };
    
    stompClient.send(
        '/app/chat.readStatus',
        {},
        JSON.stringify(readStatusPayload)
    );
}
```

### **5. Cập nhật UI hiển thị checkmark (chỉ ở tin nhắn mới nhất)**
```javascript
function updateMessageReadStatus(readStatus) {
    const chatMessages = document.getElementById('chatMessages');
    if (!chatMessages) return;
    
    // Xóa tất cả checkmark cũ (nếu có)
    const oldIndicators = chatMessages.querySelectorAll('.read-indicator');
    oldIndicators.forEach(indicator => indicator.remove());
    
    // Tìm tin nhắn mới nhất của mình (sent)
    const sentMessages = chatMessages.querySelectorAll('.message.sent');
    if (sentMessages.length === 0) return;
    
    // Lấy tin nhắn mới nhất
    const lastSentMessage = sentMessages[sentMessages.length - 1];
    
    // Thêm checkmark vào tin nhắn mới nhất
    const messageTime = lastSentMessage.querySelector('.message-time');
    if (messageTime) {
        const readIndicator = document.createElement('span');
        readIndicator.className = 'read-indicator';
        readIndicator.style.fontSize = '11px';
        readIndicator.style.marginLeft = '5px';
        readIndicator.innerHTML = ' ✓✓';
        readIndicator.style.color = '#000';
        
        messageTime.appendChild(readIndicator);
    }
}
```

### **6. Đánh dấu CHỈ tin nhắn mới nhất (cải thiện UX)**
```javascript
function markAllVisibleMessagesAsRead() {
    const chatMessages = document.getElementById('chatMessages');
    const messageElements = chatMessages.querySelectorAll('.message:not(.sent)');
    
    if (messageElements.length > 0) {
        // Chỉ lấy tin nhắn cuối cùng (mới nhất)
        const lastMessage = messageElements[messageElements.length - 1];
        const messageId = lastMessage.getAttribute('data-message-id');
        
        if (messageId) {
            // Chỉ đánh dấu tin nhắn mới nhất
            markMessageAsRead(parseInt(messageId), currentChatId);
        }
    }
}
```

### **7. Gọi tự động khi load tin nhắn**
```javascript
function displayRealMessages(messages) {
    // ... hiển thị tin nhắn ...
    
    // Tự động đánh dấu đã đọc
    setTimeout(() => {
        markAllVisibleMessagesAsRead();
    }, 1000);
}
```

## 🎯 Các trường hợp hoạt động

### **Case 1: User B mở chat có tin nhắn cũ của User A**
```
✅ Load tin nhắn từ API
✅ Hiển thị lên màn hình
✅ Sau 1 giây → Tự động gửi read status CHỈ cho TIN NHẮN MỚI NHẤT của User A
✅ User A nhận thông báo → Hiển thị ✓✓ màu đen ở TIN NHẮN MỚI NHẤT
```

### **Case 2: User A gửi tin nhắn mới khi User B đang mở chat**
```
✅ User B nhận tin nhắn qua WebSocket
✅ Hiển thị tin nhắn lên UI
✅ Sau 500ms → Tự động gửi read status
✅ User A nhận thông báo → Hiển thị ✓✓ màu đen
```

### **Case 3: User B chưa mở chat**
```
❌ User B không nhận tin nhắn realtime
❌ Không gửi read status
⏳ Khi User B mở chat → Case 1 xảy ra
```

## 🎨 UI Hiển thị (Cải thiện UX)

### **Trước đây: Tất cả tin nhắn đều có checkmark (❌ Rối mắt)**
```
┌─────────────────────────────┐
│ Hello                       │
│ 10:30 AM ✓✓                 │  ← Checkmark
└─────────────────────────────┘
┌─────────────────────────────┐
│ How are you?                │
│ 10:31 AM ✓✓                 │  ← Checkmark
└─────────────────────────────┘
┌─────────────────────────────┐
│ See you later               │
│ 10:32 AM ✓✓                 │  ← Checkmark
└─────────────────────────────┘
```

### **Bây giờ: Chỉ tin nhắn mới nhất có checkmark (✅ Gọn gàng)**
```
┌─────────────────────────────┐
│ Hello                       │
│ 10:30 AM                    │  ← Không có checkmark
└─────────────────────────────┘
┌─────────────────────────────┐
│ How are you?                │
│ 10:31 AM                    │  ← Không có checkmark
└─────────────────────────────┘
┌─────────────────────────────┐
│ See you later               │
│ 10:32 AM ✓✓                 │  ← CHỈ tin nhắn mới nhất có checkmark
└─────────────────────────────┘
```

**Lý do:**
- ✅ Giao diện gọn gàng, dễ nhìn hơn
- ✅ Giống WhatsApp, Messenger, Telegram
- ✅ User chỉ cần biết tin nhắn mới nhất đã được xem
- ✅ Ngầm hiểu: Nếu tin nhắn mới nhất đã xem → Tất cả tin nhắn trước đó cũng đã xem

## 🔍 Debug & Testing

### **Console logs để kiểm tra:**

**Khi User B mở chat:**
```javascript
📥 Subscribing to read status: /topic/chat.123.readStatus
✅ Đã subscribe vào chat: 123
👁️ Chỉ đánh dấu tin nhắn mới nhất: 456
✅ Read status đã được gửi
```

**Khi User A nhận read status:**
```javascript
👁️ Nhận read status: {chatId: 123, userId: 2, messageId: 456}
✅ Hiển thị checkmark ở tin nhắn mới nhất
```

### **Kiểm tra trong database:**
```sql
SELECT * FROM message_read_status 
WHERE message_id = 456 AND user_id = 2;

-- Kết quả:
-- message_id | user_id | read_at
-- 456        | 2       | 2025-10-17 16:30:00
```

## ⚙️ Backend đã có sẵn

### **WebSocketChatController.java**
```java
@MessageMapping("/chat.readStatus")
public void processReadStatus(@Payload ReadStatusDTO readStatusDTO, 
                              SimpMessageHeaderAccessor headerAccessor) {
    // Xác thực userId
    Integer authenticatedUserId = headerAccessor.getSessionAttributes().get("userId");
    
    // Validate
    if (!authenticatedUserId.equals(readStatusDTO.getUserId())) {
        return;
    }
    
    // Lưu vào database
    messageService.markMessageAsRead(readStatusDTO);
    
    // Broadcast
    messagingTemplate.convertAndSend(
        "/topic/chat." + chatId + ".readStatus", 
        readStatusDTO
    );
}
```

## 📊 Tổng kết

| Tính năng | Trạng thái | Ghi chú |
|-----------|-----------|---------|
| Subscribe read status topic | ✅ | `/topic/chat.{chatId}.readStatus` |
| Tự động đánh dấu tin nhắn mới | ✅ | Sau 500ms |
| Tự động đánh dấu tin nhắn cũ | ✅ | Sau 1s khi load |
| Gửi read status qua WebSocket | ✅ | `/app/chat.readStatus` |
| Nhận read status từ server | ✅ | Qua subscription |
| Hiển thị checkmark ✓✓ | ✅ | Màu đen |
| Chỉ đánh dấu tin nhắn người khác | ✅ | Không đánh dấu tin nhắn của mình |
| Lưu vào database | ✅ | `message_read_status` table |

## 🚀 Tính năng mở rộng

### **1. Hiển thị danh sách người đã xem**
```javascript
// Khi click vào ✓✓
function showReadByList(messageId) {
    // Fetch danh sách người đã đọc từ API
    // Hiển thị modal với avatar + tên + thời gian đọc
}
```

### **2. Checkmark màu xanh vs màu đen**
```javascript
// ✓ màu xám = đã gửi
// ✓✓ màu xám = đã nhận (delivered)
// ✓✓ màu đen = đã xem (read)
```

### **3. Group chat - Hiển thị số người đã xem**
```javascript
readIndicator.innerHTML = ` ✓✓ ${readCount}`;
```

## ⚠️ Lưu ý

1. **Chỉ đánh dấu tin nhắn của người khác:**
   - Không gửi read status cho tin nhắn của chính mình
   - Check: `messageData.senderId !== currentUserId`

2. **Timeout để đảm bảo UI render:**
   - Tin nhắn mới: 500ms
   - Tin nhắn cũ: 1000ms

3. **data-message-id attribute:**
   - Mọi tin nhắn phải có `data-message-id`
   - Dùng để tìm và cập nhật checkmark

4. **Unsubscribe khi đóng chat:**
   - Tự động unsubscribe khi chuyển sang chat khác
   - Tránh nhận read status của chat cũ

## 🎉 Kết quả

Tính năng Read Receipts đã hoàn thành với đầy đủ chức năng:
- ✅ Tự động đánh dấu đã đọc
- ✅ Realtime notification
- ✅ Hiển thị checkmark ✓✓ màu đen
- ✅ Chỉ đánh dấu tin nhắn của người khác
- ✅ Lưu vào database
- ✅ Hoạt động với cả tin nhắn cũ và mới
