# Hệ thống Chat - Luồng hoạt động toàn diện

## 📋 Mục lục

1. [Tổng quan kiến trúc](#tổng-quan-kiến-trúc)
2. [Luồng 1: Khởi tạo và kết nối](#luồng-1-khởi-tạo-và-kết-nối)
3. [Luồng 2: Mở chat và load lịch sử](#luồng-2-mở-chat-và-load-lịch-sử)
4. [Luồng 3: Gửi tin nhắn realtime](#luồng-3-gửi-tin-nhắn-realtime)
5. [Luồng 4: Nhận tin nhắn realtime](#luồng-4-nhận-tin-nhắn-realtime)
6. [Luồng 5: Đánh dấu đã đọc](#luồng-5-đánh-dấu-đã-đọc)
7. [Tổng kết](#tổng-kết)

---

## 🏗️ Tổng quan kiến trúc

### **Stack công nghệ:**

```
Frontend:
├── HTML/CSS/JavaScript
├── SockJS Client (WebSocket fallback)
├── STOMP.js (Messaging protocol)
└── Fetch API (REST calls)

Backend:
├── Spring Boot
├── Spring WebSocket
├── STOMP over WebSocket
├── Spring Security (Session-based)
└── MySQL Database

Database:
├── users (thông tin user)
├── chats (cuộc trò chuyện)
├── chat_members (thành viên chat)
├── messages (tin nhắn)
└── message_read_status (trạng thái đã đọc)
```

### **Endpoints:**

**REST API:**
- `GET /api/chats/{userId}` - Lấy danh sách chat
- `GET /api/messages/chat/{chatId}` - Lấy lịch sử tin nhắn
- `POST /api/chats/direct` - Tạo chat 1-1

**WebSocket:**
- `CONNECT /ws` - Kết nối WebSocket
- `SEND /app/chat.sendMessage` - Gửi tin nhắn
- `SEND /app/chat.readStatus` - Gửi trạng thái đã đọc
- `SUBSCRIBE /topic/chat.{chatId}` - Nhận tin nhắn
- `SUBSCRIBE /topic/chat.{chatId}.readStatus` - Nhận trạng thái đã đọc

---

## 🔄 Luồng 1: Khởi tạo và kết nối

### **Sơ đồ:**

```
User mở messages.html
        ↓
┌───────────────────────────────────────────────────────────┐
│ 1. DOMContentLoaded Event                                │
├───────────────────────────────────────────────────────────┤
│   loadFriends()         → GET /api/friends/{userId}      │
│   startHeartbeat()      → POST /api/presence/heartbeat   │
│   connectWebSocket()    → CONNECT /ws                    │
└───────────────────────────────────────────────────────────┘
        ↓
┌───────────────────────────────────────────────────────────┐
│ 2. WebSocket Connection                                   │
├───────────────────────────────────────────────────────────┤
│   Client: new SockJS('/ws')                              │
│   Client: Stomp.over(socket)                             │
│   Client: stompClient.connect({}, onConnected, onError)  │
│        ↓                                                  │
│   Server: HttpSessionHandshakeInterceptor                │
│           → Lấy userId từ HTTP session                   │
│           → Lưu vào WebSocket session attributes         │
│        ↓                                                  │
│   Server: AuthChannelInterceptor                         │
│           → Kiểm tra userId khi STOMP CONNECT            │
│        ↓                                                  │
│   ✅ WebSocket connected                                 │
│   isWebSocketConnected = true                            │
└───────────────────────────────────────────────────────────┘
```

### **Code chi tiết:**

**Frontend (messages.html):**
```javascript
document.addEventListener('DOMContentLoaded', function () {
    loadFriends();      // Load danh sách bạn bè
    startHeartbeat();   // Gửi heartbeat mỗi 2 phút
    connectWebSocket(); // Kết nối WebSocket
});

function connectWebSocket() {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    
    stompClient.connect({}, 
        function onConnected(frame) {
            isWebSocketConnected = true;
            console.log('✅ WebSocket connected');
        },
        function onError(error) {
            console.error('❌ WebSocket error:', error);
            setTimeout(connectWebSocket, 5000); // Retry
        }
    );
}
```

**Backend (HttpSessionHandshakeInterceptor.java):**
```java
@Override
public boolean beforeHandshake(ServerHttpRequest request, ...) {
    HttpSession session = servletRequest.getServletRequest().getSession(false);
    
    if (session != null) {
        Object userId = session.getAttribute("userId");
        if (userId != null) {
            attributes.put("userId", userId);
            return true; // Cho phép kết nối
        }
    }
    
    return false; // Từ chối kết nối
}
```

---

## 📂 Luồng 2: Mở chat và load lịch sử

### **Sơ đồ:**

```
User click vào friend trong danh sách
        ↓
┌───────────────────────────────────────────────────────────┐
│ 1. openChat(friendId, chatName, avatarUrl)               │
├───────────────────────────────────────────────────────────┤
│   findOrCreateDirectChat(friendId)                       │
│        ↓                                                  │
│   POST /api/chats/direct                                 │
│   { user1Id: currentUserId, user2Id: friendId }          │
│        ↓                                                  │
│   Server: ChatController.createDirectChat()              │
│           → Tìm chat hiện có HOẶC tạo mới                │
│           → Return chatId                                │
│        ↓                                                  │
│   ✅ Có chatId                                           │
└───────────────────────────────────────────────────────────┘
        ↓
┌───────────────────────────────────────────────────────────┐
│ 2. loadRealChatContent(chatId, chatName, avatarUrl)     │
├───────────────────────────────────────────────────────────┤
│   Hiển thị chat interface                                │
│        ↓                                                  │
│   loadMessagesFromAPI(chatId, currentUserId)            │
│        ↓                                                  │
│   GET /api/messages/chat/{chatId}?userId={userId}       │
│        ↓                                                  │
│   Server: MessageController.getMessagesByChatId()        │
│           → Query messages từ database                   │
│           → Join với users để lấy sender info           │
│           → Return List<MessageResponseDTO>              │
│        ↓                                                  │
│   displayRealMessages(messages)                          │
│        ↓                                                  │
│   Hiển thị tin nhắn lên UI                              │
└───────────────────────────────────────────────────────────┘
        ↓
┌───────────────────────────────────────────────────────────┐
│ 3. Subscribe WebSocket Topics                            │
├───────────────────────────────────────────────────────────┤
│   subscribeToChat(chatId)                                │
│        ↓                                                  │
│   SUBSCRIBE /topic/chat.{chatId}                         │
│   → Nhận tin nhắn mới                                   │
│        ↓                                                  │
│   SUBSCRIBE /topic/chat.{chatId}.readStatus              │
│   → Nhận thông báo đã đọc                               │
│        ↓                                                  │
│   ✅ Sẵn sàng chat realtime                             │
└───────────────────────────────────────────────────────────┘
        ↓
┌───────────────────────────────────────────────────────────┐
│ 4. Auto Mark as Read                                     │
├───────────────────────────────────────────────────────────┤
│   setTimeout(() => {                                     │
│       markAllVisibleMessagesAsRead()                     │
│       → Chỉ đánh dấu tin nhắn MỚI NHẤT của người khác   │
│   }, 1000)                                               │
└───────────────────────────────────────────────────────────┘
```

### **Code chi tiết:**

**Frontend:**
```javascript
async function openChat(friendId, chatName, avatarUrl) {
    // 1. Tìm hoặc tạo chat
    const chatId = await findOrCreateDirectChat(friendId);
    
    // 2. Load nội dung chat
    await loadRealChatContent(chatId, chatName, avatarUrl, currentUserId);
}

async function loadRealChatContent(chatId, chatName, avatarUrl, currentUserId) {
    // Hiển thị UI
    chatArea.innerHTML = `<div class="chat-header">...</div>`;
    
    // Load tin nhắn từ API
    await loadMessagesFromAPI(chatId, currentUserId);
    
    // Subscribe WebSocket
    subscribeToChat(chatId);
}

function subscribeToChat(chatId) {
    // Subscribe tin nhắn
    currentSubscription = stompClient.subscribe(
        '/topic/chat.' + chatId, 
        onMessageReceived
    );
    
    // Subscribe read status
    currentReadStatusSubscription = stompClient.subscribe(
        '/topic/chat.' + chatId + '.readStatus',
        onReadStatusReceived
    );
}
```

---

## 📤 Luồng 3: Gửi tin nhắn realtime

### **Sơ đồ:**

```
User nhập tin nhắn và nhấn Enter/Send
        ↓
┌───────────────────────────────────────────────────────────┐
│ 1. Client: sendRealMessage()                             │
├───────────────────────────────────────────────────────────┤
│   Validate: content không rỗng                           │
│   Validate: currentChatId tồn tại                        │
│   Validate: WebSocket đã kết nối                         │
│        ↓                                                  │
│   Tạo payload:                                           │
│   {                                                       │
│     chatId: 123,                                         │
│     senderId: 1,                                         │
│     content: "Hello",                                    │
│     messageType: "TEXT"                                  │
│   }                                                       │
│        ↓                                                  │
│   SEND /app/chat.sendMessage                             │
│   stompClient.send('/app/chat.sendMessage', {}, JSON)    │
└───────────────────────────────────────────────────────────┘
        ↓
┌───────────────────────────────────────────────────────────┐
│ 2. Server: WebSocketChatController.processMessage()     │
├───────────────────────────────────────────────────────────┤
│   Lấy authenticatedUserId từ WebSocket session           │
│        ↓                                                  │
│   Validate: authenticatedUserId == senderId              │
│   → Nếu không khớp: return (từ chối)                    │
│        ↓                                                  │
│   messageService.saveAndBroadcastMessage(dto)            │
│        ↓                                                  │
│   INSERT INTO messages (chat_id, sender_id, content, ...)│
│        ↓                                                  │
│   Return ChatMessageResponseDTO {                        │
│     id: 456,                                             │
│     chatId: 123,                                         │
│     senderId: 1,                                         │
│     senderName: "User A",                                │
│     content: "Hello",                                    │
│     timestamp: "2025-10-17T16:30:00"                     │
│   }                                                       │
└───────────────────────────────────────────────────────────┘
        ↓
┌───────────────────────────────────────────────────────────┐
│ 3. Server: Broadcast to Topic                           │
├───────────────────────────────────────────────────────────┤
│   messagingTemplate.convertAndSend(                      │
│       "/topic/chat.123",                                 │
│       responseDTO                                        │
│   )                                                       │
│        ↓                                                  │
│   ✅ Gửi đến TẤT CẢ subscribers của /topic/chat.123     │
└───────────────────────────────────────────────────────────┘
```

### **Code chi tiết:**

**Frontend:**
```javascript
function sendRealMessage() {
    const content = messageInput.value.trim();
    
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
    
    messageInput.value = ''; // Xóa input
}
```

**Backend:**
```java
@MessageMapping("/chat.sendMessage")
public void processMessage(@Payload ChatMessageDTO dto, 
                          SimpMessageHeaderAccessor headerAccessor) {
    // Xác thực
    Integer authenticatedUserId = headerAccessor.getSessionAttributes().get("userId");
    if (!authenticatedUserId.equals(dto.getSenderId())) {
        return; // Từ chối
    }
    
    // Lưu database
    ChatMessageResponseDTO response = messageService.saveAndBroadcastMessage(dto);
    
    // Broadcast
    messagingTemplate.convertAndSend(
        "/topic/chat." + dto.getChatId(), 
        response
    );
}
```

---

## 📥 Luồng 4: Nhận tin nhắn realtime

### **Sơ đồ:**

```
Server broadcast tin nhắn đến /topic/chat.123
        ↓
┌───────────────────────────────────────────────────────────┐
│ Client A (Sender)                                        │
├───────────────────────────────────────────────────────────┤
│   Subscription callback được gọi                         │
│        ↓                                                  │
│   onMessageReceived(message)                             │
│        ↓                                                  │
│   messageData = JSON.parse(message.body)                 │
│   {                                                       │
│     id: 456,                                             │
│     senderId: 1,  ← Là userId của mình                  │
│     content: "Hello",                                    │
│     ...                                                   │
│   }                                                       │
│        ↓                                                  │
│   displayReceivedMessage(messageData)                    │
│        ↓                                                  │
│   isCurrentUser = (senderId === currentUserId)           │
│   → true                                                 │
│        ↓                                                  │
│   Hiển thị tin nhắn bên PHẢI (sent)                     │
│   <div class="message sent">...</div>                    │
└───────────────────────────────────────────────────────────┘

┌───────────────────────────────────────────────────────────┐
│ Client B (Receiver)                                      │
├───────────────────────────────────────────────────────────┤
│   Subscription callback được gọi                         │
│        ↓                                                  │
│   onMessageReceived(message)                             │
│        ↓                                                  │
│   messageData = JSON.parse(message.body)                 │
│   {                                                       │
│     id: 456,                                             │
│     senderId: 1,  ← KHÔNG phải userId của mình          │
│     content: "Hello",                                    │
│     ...                                                   │
│   }                                                       │
│        ↓                                                  │
│   displayReceivedMessage(messageData)                    │
│        ↓                                                  │
│   isCurrentUser = (senderId === currentUserId)           │
│   → false                                                │
│        ↓                                                  │
│   Hiển thị tin nhắn bên TRÁI (received)                 │
│   <div class="message">...</div>                         │
│        ↓                                                  │
│   Tự động đánh dấu đã đọc sau 500ms                     │
│   setTimeout(() => {                                     │
│       markMessageAsRead(456, 123)                        │
│   }, 500)                                                │
└───────────────────────────────────────────────────────────┘
```

### **Code chi tiết:**

**Frontend:**
```javascript
currentSubscription = stompClient.subscribe('/topic/chat.' + chatId, function(message) {
    const messageData = JSON.parse(message.body);
    
    // Hiển thị tin nhắn
    displayReceivedMessage(messageData);
    
    // Tự động đánh dấu đã đọc nếu không phải tin nhắn của mình
    if (messageData.senderId !== currentUserId) {
        setTimeout(() => {
            markMessageAsRead(messageData.id, chatId);
        }, 500);
    }
});

function displayReceivedMessage(messageData) {
    const isCurrentUser = (messageData.senderId === currentUserId);
    
    const messageDiv = createRealMessageElement({
        id: messageData.id,
        content: messageData.content,
        isCurrentUser: isCurrentUser,
        time: formatMessageTime(messageData.timestamp)
    });
    
    chatMessages.appendChild(messageDiv);
    chatMessages.scrollTop = chatMessages.scrollHeight;
}
```

---

## ✓✓ Luồng 5: Đánh dấu đã đọc

### **Sơ đồ:**

```
User B nhận tin nhắn từ User A
        ↓
┌───────────────────────────────────────────────────────────┐
│ 1. Client B: Auto Mark as Read                          │
├───────────────────────────────────────────────────────────┤
│   Sau 500ms (tin nhắn mới) hoặc 1s (tin nhắn cũ)        │
│        ↓                                                  │
│   markMessageAsRead(messageId, chatId)                   │
│        ↓                                                  │
│   Tạo payload:                                           │
│   {                                                       │
│     chatId: 123,                                         │
│     userId: 2,  ← User B                                │
│     messageId: 456                                       │
│   }                                                       │
│        ↓                                                  │
│   SEND /app/chat.readStatus                              │
│   stompClient.send('/app/chat.readStatus', {}, JSON)     │
└───────────────────────────────────────────────────────────┘
        ↓
┌───────────────────────────────────────────────────────────┐
│ 2. Server: WebSocketChatController.processReadStatus()  │
├───────────────────────────────────────────────────────────┤
│   Lấy authenticatedUserId từ WebSocket session           │
│        ↓                                                  │
│   Validate: authenticatedUserId == userId                │
│   → Nếu không khớp: return (từ chối)                    │
│        ↓                                                  │
│   messageService.markMessageAsRead(dto)                  │
│        ↓                                                  │
│   INSERT INTO message_read_status                        │
│   (message_id, user_id, read_at)                         │
│   VALUES (456, 2, NOW())                                 │
│        ↓                                                  │
│   Broadcast read status                                  │
│   messagingTemplate.convertAndSend(                      │
│       "/topic/chat.123.readStatus",                      │
│       readStatusDTO                                      │
│   )                                                       │
└───────────────────────────────────────────────────────────┘
        ↓
┌───────────────────────────────────────────────────────────┐
│ 3. Client A: Receive Read Status                        │
├───────────────────────────────────────────────────────────┤
│   Subscription callback được gọi                         │
│        ↓                                                  │
│   onReadStatusReceived(message)                          │
│        ↓                                                  │
│   readStatus = JSON.parse(message.body)                  │
│   { chatId: 123, userId: 2, messageId: 456 }            │
│        ↓                                                  │
│   updateMessageReadStatus(readStatus)                    │
│        ↓                                                  │
│   Xóa TẤT CẢ checkmark cũ                               │
│   querySelectorAll('.read-indicator').remove()           │
│        ↓                                                  │
│   Tìm tin nhắn MỚI NHẤT của mình (sent)                 │
│   lastSentMessage = querySelectorAll('.message.sent')[-1]│
│        ↓                                                  │
│   Thêm checkmark ✓✓ vào tin nhắn mới nhất              │
│   <span class="read-indicator"> ✓✓</span>               │
│        ↓                                                  │
│   ✅ User A thấy tin nhắn đã được xem                   │
└───────────────────────────────────────────────────────────┘
```

### **Code chi tiết:**

**Frontend (Client B - Gửi read status):**
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

**Backend:**
```java
@MessageMapping("/chat.readStatus")
public void processReadStatus(@Payload ReadStatusDTO dto,
                              SimpMessageHeaderAccessor headerAccessor) {
    // Xác thực
    Integer authenticatedUserId = headerAccessor.getSessionAttributes().get("userId");
    if (!authenticatedUserId.equals(dto.getUserId())) {
        return;
    }
    
    // Lưu database
    messageService.markMessageAsRead(dto);
    
    // Broadcast
    messagingTemplate.convertAndSend(
        "/topic/chat." + dto.getChatId() + ".readStatus",
        dto
    );
}
```

**Frontend (Client A - Nhận read status):**
```javascript
currentReadStatusSubscription = stompClient.subscribe(
    '/topic/chat.' + chatId + '.readStatus',
    function(message) {
        const readStatus = JSON.parse(message.body);
        updateMessageReadStatus(readStatus);
    }
);

function updateMessageReadStatus(readStatus) {
    // Xóa tất cả checkmark cũ
    document.querySelectorAll('.read-indicator').forEach(el => el.remove());
    
    // Tìm tin nhắn mới nhất của mình
    const sentMessages = document.querySelectorAll('.message.sent');
    const lastMessage = sentMessages[sentMessages.length - 1];
    
    // Thêm checkmark
    const indicator = document.createElement('span');
    indicator.className = 'read-indicator';
    indicator.innerHTML = ' ✓✓';
    indicator.style.color = '#000';
    
    lastMessage.querySelector('.message-time').appendChild(indicator);
}
```

---

## 📊 Tổng kết

### **Toàn bộ luồng từ đầu đến cuối:**

```
┌─────────────────────────────────────────────────────────────────────┐
│                     KHỞI TẠO HỆ THỐNG                              │
└─────────────────────────────────────────────────────────────────────┘
User mở messages.html
    → Load danh sách bạn bè (REST API)
    → Kết nối WebSocket (/ws)
    → Xác thực qua HTTP session
    → ✅ WebSocket connected

┌─────────────────────────────────────────────────────────────────────┐
│                     MỞ CHAT VÀ LOAD LỊCH SỬ                        │
└─────────────────────────────────────────────────────────────────────┘
User click vào friend
    → Tạo/tìm chat (REST API)
    → Load lịch sử tin nhắn (REST API)
    → Subscribe /topic/chat.{chatId}
    → Subscribe /topic/chat.{chatId}.readStatus
    → Tự động đánh dấu tin nhắn mới nhất đã đọc
    → ✅ Sẵn sàng chat realtime

┌─────────────────────────────────────────────────────────────────────┐
│                     GỬI TIN NHẮN REALTIME                          │
└─────────────────────────────────────────────────────────────────────┘
User A nhập và gửi tin nhắn
    → SEND /app/chat.sendMessage (WebSocket)
    → Server xác thực senderId
    → Server lưu vào database
    → Server broadcast đến /topic/chat.{chatId}
    → User A nhận tin nhắn (hiển thị bên phải)
    → User B nhận tin nhắn (hiển thị bên trái)
    → ✅ Cả 2 thấy tin nhắn ngay lập tức

┌─────────────────────────────────────────────────────────────────────┐
│                     ĐÁNH DẤU ĐÃ ĐỌC                               │
└─────────────────────────────────────────────────────────────────────┘
User B nhận tin nhắn
    → Tự động gửi read status sau 500ms
    → SEND /app/chat.readStatus (WebSocket)
    → Server xác thực userId
    → Server lưu vào message_read_status
    → Server broadcast đến /topic/chat.{chatId}.readStatus
    → User A nhận read status
    → Xóa checkmark cũ, thêm checkmark mới
    → ✅ User A thấy ✓✓ ở tin nhắn mới nhất
```

### **Công nghệ sử dụng:**

| Layer | Công nghệ | Mục đích |
|-------|-----------|----------|
| **Transport** | SockJS | WebSocket với fallback |
| **Protocol** | STOMP | Messaging protocol |
| **Backend** | Spring WebSocket | WebSocket server |
| **Auth** | HTTP Session | Xác thực user |
| **Database** | MySQL | Lưu trữ tin nhắn |
| **Frontend** | Vanilla JS | UI và logic |

### **Bảo mật:**

✅ **Session-based authentication** - Chỉ user đã login mới kết nối được  
✅ **Validate senderId** - Không thể giả mạo người gửi  
✅ **Validate userId** - Không thể đánh dấu đã đọc cho người khác  
✅ **CORS restriction** - Giới hạn origins được phép  

### **Performance:**

✅ **Realtime messaging** - Tin nhắn đến ngay lập tức  
✅ **Optimized read receipts** - Chỉ đánh dấu tin nhắn mới nhất  
✅ **Auto reconnect** - Tự động kết nối lại khi mất kết nối  
✅ **Efficient broadcasting** - Chỉ gửi đến subscribers  

### **UX:**

✅ **Giao diện gọn gàng** - Checkmark chỉ ở tin nhắn mới nhất  
✅ **Giống app phổ biến** - WhatsApp, Messenger style  
✅ **Auto mark as read** - Không cần thao tác thủ công  
✅ **Realtime feedback** - Biết ngay khi tin nhắn được xem  

---

## 🎉 Kết luận

Hệ thống chat đã hoàn thiện với đầy đủ tính năng:

1. ✅ **REST API** - Load danh sách chat và lịch sử tin nhắn
2. ✅ **WebSocket Realtime** - Gửi/nhận tin nhắn ngay lập tức
3. ✅ **Read Receipts** - Đánh dấu và hiển thị đã xem
4. ✅ **Authentication** - Bảo mật session-based
5. ✅ **UX tối ưu** - Giao diện đẹp, dễ sử dụng

**Tài liệu tham khảo:**
- `README-CHAT.md` - Tổng quan hệ thống chat
- `WEBSOCKET-CLIENT-GUIDE.md` - Hướng dẫn WebSocket client
- `READ-RECEIPTS-GUIDE.md` - Hướng dẫn Read Receipts
- `READ-RECEIPTS-UX-IMPROVEMENT.md` - Cải thiện UX
