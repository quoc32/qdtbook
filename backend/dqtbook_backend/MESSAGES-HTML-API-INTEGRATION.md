# Messages.html - Tích hợp API Chat Thực Tế

## Tóm tắt thay đổi

Đã cập nhật file `messages.html` để tích hợp với các API chat thực tế thay vì sử dụng dữ liệu demo.

## Các thay đổi chính

### 1. **Function `openChat()` - Hoàn toàn mới**

**Trước:**
- Chỉ set `currentChatId = friendId`
- Load demo messages ngay lập tức

**Sau:**
- ✅ Gọi API `find-or-create-direct` để tạo/tìm chat
- ✅ Xử lý loading state và error handling
- ✅ Load tin nhắn thật từ API
- ✅ Cập nhật UI với dữ liệu thực tế

### 2. **API Integration Functions (Mới)**

#### `findOrCreateDirectChat(userId1, userId2)`
- Gọi API `POST /api/chats/find-or-create-direct`
- Trả về `chatId` nếu thành công
- Handle error với message rõ ràng

#### `loadRealChatContent(chatId, chatName, avatarUrl, currentUserId)`
- Thay thế `loadChatContent()` demo
- Tạo UI chat với loading state
- Gọi `loadMessagesFromAPI()` để load tin nhắn

#### `loadMessagesFromAPI(chatId, currentUserId)`
- Gọi API `GET /api/messages/chat/{chatId}?userId={userId}`
- Transform dữ liệu từ API format sang display format
- Handle empty state và error state

#### `sendRealMessage()`
- Thay thế `sendMessage()` demo
- Gọi API `POST /api/messages` để gửi tin nhắn
- Disable input khi đang gửi
- Add tin nhắn vào UI ngay lập tức

### 3. **UI State Management (Mới)**

#### `showChatLoadingState()`
- Hiển thị spinner khi đang mở chat
- Disable input area

#### `showChatErrorState()`
- Hiển thị error message khi có lỗi
- Button "Thử lại" để retry

### 4. **Message Display Functions (Cập nhật)**

#### `displayRealMessages(messages)`
- Replace `displayMessages()` demo
- Handle empty state với message thân thiện
- Support cả text và media messages

#### `createRealMessageElement(message)`
- Replace `createMessageElement()` demo  
- Support media URLs
- Improved time formatting

#### `formatMessageTime(timestamp)`
- Parse ISO timestamp từ API
- Format theo locale Việt Nam
- Show ngày nếu tin nhắn cũ

### 5. **Chat Info Enhancement**

#### `loadChatInfo()` - Cập nhật
- Hiển thị real `chatId`
- Thêm action "Đánh dấu đã đọc tất cả"
- Thêm action "Xóa cuộc trò chuyện"

#### `markAllAsRead(chatId)` - Mới
- Gọi API `POST /api/messages/chat/{chatId}/read-all`
- Mark tất cả messages là đã đọc

## API Endpoints được sử dụng

| Endpoint | Method | Purpose | Status |
|----------|--------|---------|--------|
| `/api/chats/find-or-create-direct` | POST | Tạo/tìm chat giữa 2 users | ✅ Implemented |
| `/api/messages/chat/{chatId}` | GET | Lấy tin nhắn của chat | ✅ Implemented |
| `/api/messages` | POST | Gửi tin nhắn mới | ✅ Implemented |
| `/api/messages/chat/{chatId}/read-all` | POST | Đánh dấu tất cả đã đọc | ✅ Implemented |

## Data Flow

```
1. User clicks chat-item
   ↓
2. openChat(friendId, chatName, avatarUrl)
   ↓
3. showChatLoadingState()
   ↓
4. findOrCreateDirectChat(currentUserId, friendId)
   ↓ API: POST /api/chats/find-or-create-direct
5. Get chatId from response
   ↓
6. loadRealChatContent(chatId, ...)
   ↓
7. loadMessagesFromAPI(chatId, currentUserId)
   ↓ API: GET /api/messages/chat/{chatId}
8. displayRealMessages(messages)
   ↓
9. Setup sendRealMessage() for input
   ↓ API: POST /api/messages (when user sends)
10. Add new message to UI immediately
```

## Error Handling

### Loading Chat
- ❌ User not logged in → Alert "Đăng nhập lại"
- ❌ API call fails → showChatErrorState() với retry button
- ❌ Chat creation fails → Error message với chi tiết

### Loading Messages  
- ❌ API fails → Show retry button trong chat area
- ❌ Empty chat → Show friendly empty state message

### Sending Messages
- ❌ Send fails → Alert với error message
- ❌ Input disabled during send → Prevent spam
- ✅ Auto re-enable input after success/error

## UI/UX Improvements

### Loading States
- 🔄 Spinner khi mở chat
- 🔄 Loading messages indicator  
- 🔄 Input disabled khi gửi tin nhắn

### Empty States
- 📭 "Chưa có tin nhắn nào" với friendly message
- 📭 "Chưa có bạn bè" với hướng dẫn

### Error States
- ❌ Error messages rõ ràng
- 🔄 Retry buttons
- 🚨 Visual indicators (màu đỏ, icons)

## Tương thích ngược

- ✅ Giữ nguyên UI/CSS design
- ✅ Giữ nguyên search functionality
- ✅ Giữ nguyên online/offline indicators
- ✅ Giữ nguyên responsive design

## Testing

### Test Manual
1. Click vào chat-item → Kiểm tra loading state
2. Verify API calls trong Network tab
3. Test gửi tin nhắn → Verify POST /api/messages
4. Test error cases với invalid IDs
5. Test empty chat state
6. Test mark all as read functionality

### Test Edge Cases
- User chưa đăng nhập
- API server offline
- Chat không tồn tại
- Messages load failed
- Send message failed

## Các tính năng sẽ thêm sau

- [ ] Real-time messaging với WebSocket
- [ ] Message reactions (like, love, etc.)
- [ ] File/image upload
- [ ] Message search
- [ ] Delete messages
- [ ] Forward messages
- [ ] Message threading/replies