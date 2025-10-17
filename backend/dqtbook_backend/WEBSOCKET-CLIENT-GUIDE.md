# WebSocket Client Implementation - Hướng dẫn

## ✅ Đã hoàn thành

WebSocket đã được tích hợp vào `messages.html` để chat realtime.

## 🔧 Các thay đổi đã thực hiện

### 1. **Thêm thư viện WebSocket** (trong `<head>`)
```html
<!-- WebSocket Libraries -->
<script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
```

### 2. **Khai báo biến WebSocket**
```javascript
let stompClient = null;              // STOMP client cho WebSocket
let currentSubscription = null;      // Subscription hiện tại cho chat
let isWebSocketConnected = false;    // Trạng thái kết nối WebSocket
```

### 3. **Kết nối WebSocket khi load trang**
```javascript
document.addEventListener('DOMContentLoaded', function () {
    loadFriends();
    startHeartbeat();
    startFriendsRefresh();
    connectWebSocket();  // ← Kết nối WebSocket
});
```

### 4. **Hàm connectWebSocket()**
- Tạo SockJS connection đến `/ws`
- Tạo STOMP client
- Xử lý kết nối thành công/thất bại
- Tự động reconnect sau 5 giây nếu lỗi

### 5. **Hàm subscribeToChat(chatId)**
- Unsubscribe chat cũ (nếu có)
- Subscribe vào topic `/topic/chat.{chatId}`
- Nhận tin nhắn realtime và hiển thị lên UI

### 6. **Hàm sendRealMessage()** - ĐÃ THAY ĐỔI
**Trước (REST API):**
```javascript
// Gửi qua REST API POST /api/messages
const response = await fetch('/api/messages', {
    method: 'POST',
    body: JSON.stringify({...})
});
```

**Sau (WebSocket):**
```javascript
// Gửi qua WebSocket /app/chat.sendMessage
stompClient.send('/app/chat.sendMessage', {}, JSON.stringify({
    chatId: currentChatId,
    senderId: currentUserId,
    content: content,
    messageType: 'TEXT'
}));
```

### 7. **Hàm displayReceivedMessage(messageData)**
- Nhận tin nhắn từ WebSocket subscription
- Kiểm tra tin nhắn của mình hay người khác
- Hiển thị lên UI với style phù hợp

## 🔄 Luồng hoạt động

### **Khi user mở trang messages.html:**
```
1. Load trang
   ↓
2. connectWebSocket()
   → Kết nối đến /ws
   → Xác thực qua HTTP session
   ↓
3. WebSocket connected ✅
   → isWebSocketConnected = true
```

### **Khi user click vào một chat:**
```
1. openChat(friendId, chatName, avatarUrl)
   ↓
2. findOrCreateDirectChat()
   → Tạo/tìm chatId
   ↓
3. loadRealChatContent(chatId, ...)
   → Load lịch sử tin nhắn từ REST API
   → subscribeToChat(chatId)  ← Subscribe WebSocket
   ↓
4. Đã subscribe vào /topic/chat.{chatId} ✅
   → Sẵn sàng nhận tin nhắn realtime
```

### **Khi user gửi tin nhắn:**
```
User A                          Server                          User B
  │                               │                               │
  │ 1. Nhập "Hello"               │                               │
  │ 2. Enter                      │                               │
  │                               │                               │
  │ 3. sendRealMessage()          │                               │
  │    stompClient.send(          │                               │
  │      '/app/chat.sendMessage'  │                               │
  │    )                          │                               │
  ├──────────────────────────────>│                               │
  │                               │                               │
  │                               │ 4. Xác thực userId            │
  │                               │ 5. Lưu vào database           │
  │                               │ 6. Broadcast                  │
  │                               │    /topic/chat.123            │
  │<──────────────────────────────┼──────────────────────────────>│
  │                               │                               │
  │ 7. displayReceivedMessage()   │    7. displayReceivedMessage()│
  │    → Hiển thị "Hello" (sent)  │       → Hiển thị "Hello" (received)│
```

## 📝 Code quan trọng

### **Gửi tin nhắn qua WebSocket:**
```javascript
function sendRealMessage() {
    const messagePayload = {
        chatId: currentChatId,
        senderId: currentUserId,
        content: content,
        messageType: 'TEXT'
    };
    
    stompClient.send(
        '/app/chat.sendMessage',
        {},
        JSON.stringify(messagePayload)
    );
    
    // Tin nhắn sẽ được hiển thị khi nhận lại từ server
    // KHÔNG thêm vào UI ngay lập tức
}
```

### **Nhận tin nhắn từ WebSocket:**
```javascript
currentSubscription = stompClient.subscribe('/topic/chat.' + chatId, function(message) {
    const messageData = JSON.parse(message.body);
    displayReceivedMessage(messageData);
});
```

### **Hiển thị tin nhắn:**
```javascript
function displayReceivedMessage(messageData) {
    const isCurrentUser = (messageData.senderId === currentUserId);
    
    const newMessage = {
        id: messageData.id,
        content: messageData.content,
        isCurrentUser: isCurrentUser,
        time: formatMessageTime(messageData.timestamp),
        senderName: messageData.senderName,
        senderAvatar: messageData.senderAvatar
    };
    
    const messageDiv = createRealMessageElement(newMessage);
    chatMessages.appendChild(messageDiv);
    chatMessages.scrollTop = chatMessages.scrollHeight;
}
```

## 🧪 Testing

### **Test 1: Kết nối WebSocket**
1. Mở trang messages.html
2. Mở Console (F12)
3. Kiểm tra log:
   ```
   🔌 Đang kết nối WebSocket...
   ✅ WebSocket connected
   🎉 Sẵn sàng chat realtime!
   ```

### **Test 2: Subscribe chat**
1. Click vào một chat
2. Kiểm tra log:
   ```
   📥 Subscribing to: /topic/chat.123
   ✅ Đã subscribe vào chat: 123
   ```

### **Test 3: Gửi tin nhắn**
1. Nhập tin nhắn "Hello"
2. Enter hoặc click Send
3. Kiểm tra log:
   ```
   📤 Gửi tin nhắn qua WebSocket đến chat 123
   ✅ Tin nhắn đã được gửi qua WebSocket
   📨 Nhận tin nhắn mới: {id: 456, content: "Hello", ...}
   ```
4. Tin nhắn hiển thị bên phải (sent)

### **Test 4: Nhận tin nhắn từ người khác**
1. Mở 2 browser/tab khác nhau
2. Login 2 user khác nhau
3. Cả 2 mở cùng chat
4. User A gửi tin nhắn
5. User B nhận ngay lập tức (không cần refresh)

## ⚠️ Lưu ý

### **1. Session Cookie**
- WebSocket sử dụng HTTP session để xác thực
- Cookie `JSESSIONID` phải được gửi kèm
- SockJS tự động xử lý cookies

### **2. Reconnection**
- Nếu WebSocket bị disconnect, sẽ tự động reconnect sau 5 giây
- Khi reconnect thành công, cần subscribe lại chat hiện tại

### **3. Tin nhắn hiển thị 2 lần?**
- **KHÔNG** - Tin nhắn chỉ hiển thị 1 lần
- Khi gửi: KHÔNG thêm vào UI ngay
- Đợi nhận lại từ server qua subscription → Mới hiển thị
- Cả người gửi và người nhận đều nhận từ subscription

### **4. Load lịch sử tin nhắn**
- Khi mở chat: Load từ REST API `/api/messages/chat/{chatId}`
- Sau đó subscribe WebSocket để nhận tin nhắn mới
- Tin nhắn cũ: từ API
- Tin nhắn mới: từ WebSocket

### **5. CORS**
- WebSocket endpoint `/ws` đã cấu hình CORS
- Hiện tại: `setAllowedOriginPatterns("*")`
- Production: Nên giới hạn origins cụ thể

## 🚀 Tính năng có thể mở rộng

### **1. Typing Indicator** (đang nhập tin nhắn)
```javascript
messageInput.addEventListener('input', function() {
    stompClient.send('/app/chat.typing', {}, JSON.stringify({
        chatId: currentChatId,
        userId: currentUserId,
        typing: true
    }));
    
    clearTimeout(typingTimeout);
    typingTimeout = setTimeout(() => {
        stompClient.send('/app/chat.typing', {}, JSON.stringify({
            chatId: currentChatId,
            userId: currentUserId,
            typing: false
        }));
    }, 3000);
});
```

### **2. Read Receipts** (đã đọc)
```javascript
function markMessageAsRead(messageId) {
    stompClient.send('/app/chat.readStatus', {}, JSON.stringify({
        chatId: currentChatId,
        userId: currentUserId,
        messageId: messageId
    }));
}
```

### **3. Online Status**
- Subscribe `/topic/user.{userId}.presence`
- Hiển thị online/offline status realtime

## 📚 Tài liệu tham khảo

- [SockJS Client](https://github.com/sockjs/sockjs-client)
- [STOMP.js](https://stomp-js.github.io/)
- [Spring WebSocket](https://docs.spring.io/spring-framework/reference/web/websocket.html)
