# OAuth2 Email Domain Restriction - Hướng dẫn đầy đủ

## 🎯 Tính năng

Giới hạn đăng nhập Google OAuth2 chỉ cho phép email:
- ✅ `@hcmute.edu.vn` (Email giảng viên/nhân viên)
- ✅ `@student.hcmute.edu.vn` (Email sinh viên)
- ❌ Từ chối tất cả email khác (Gmail, Yahoo, etc.)

Khi đăng nhập thất bại → **Redirect về trang login với thông báo lỗi rõ ràng**

---

## 📁 Files đã thay đổi

### **1. CustomOAuth2UserService.java**
**Chức năng:** Validate email domain khi user login bằng Google

```java
// Validate email domain - Chỉ cho phép @hcmute.edu.vn hoặc @student.hcmute.edu.vn
if (email == null || email.trim().isEmpty()) {
    throw new OAuth2AuthenticationException("Email is required");
}

String emailLower = email.toLowerCase().trim();
boolean isValidDomain = emailLower.endsWith("@hcmute.edu.vn") || 
                        emailLower.endsWith("@student.hcmute.edu.vn");

if (!isValidDomain) {
    System.err.println("❌ OAuth2 login rejected: Invalid email domain - " + email);
    throw new OAuth2AuthenticationException(
        "Chỉ cho phép đăng nhập bằng email @hcmute.edu.vn hoặc @student.hcmute.edu.vn"
    );
}

System.out.println("✅ Email domain validated: " + email);
```

### **2. OAuth2LoginFailureHandler.java** (NEW)
**Chức năng:** Xử lý khi OAuth2 login thất bại, redirect về login page với error message

```java
@Component
public class OAuth2LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, 
                                       HttpServletResponse response,
                                       AuthenticationException exception) throws IOException, ServletException {
        
        String errorMessage = "Đăng nhập thất bại";
        
        // Lấy thông báo lỗi chi tiết
        if (exception instanceof OAuth2AuthenticationException) {
            OAuth2AuthenticationException oauth2Exception = (OAuth2AuthenticationException) exception;
            errorMessage = oauth2Exception.getError().getDescription();
            
            if (errorMessage == null || errorMessage.isEmpty()) {
                errorMessage = oauth2Exception.getMessage();
            }
        } else {
            errorMessage = exception.getMessage();
        }
        
        // Log lỗi
        System.err.println("❌ OAuth2 Login Failed: " + errorMessage);
        
        // Encode error message
        String encodedError = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);
        
        // Redirect về trang login với error message
        String redirectUrl = "/views/login?error=oauth2&message=" + encodedError;
        
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
```

### **3. SecurityConfig.java**
**Chức năng:** Cấu hình Spring Security để sử dụng failure handler

```java
private final OAuth2LoginFailureHandler oauth2LoginFailureHandler;

public SecurityConfig(CustomOAuth2UserService customOAuth2UserService,
                     OAuth2LoginSuccessHandler oauth2LoginSuccessHandler,
                     OAuth2LoginFailureHandler oauth2LoginFailureHandler) {
    this.customOAuth2UserService = customOAuth2UserService;
    this.oauth2LoginSuccessHandler = oauth2LoginSuccessHandler;
    this.oauth2LoginFailureHandler = oauth2LoginFailureHandler;
}

// ...

.oauth2Login(oauth2 -> oauth2
    .loginPage("/login")
    .userInfoEndpoint(userInfo -> userInfo
        .userService(customOAuth2UserService)
    )
    .successHandler(oauth2LoginSuccessHandler)
    .failureHandler(oauth2LoginFailureHandler)  // ← Thêm dòng này
);
```

### **4. login.html**
**Chức năng:** Hiển thị thông báo lỗi OAuth2 và hướng dẫn user

**HTML - Thông báo lỗi:**
```html
<!-- Thông báo lỗi OAuth2 -->
<div id="oauth2ErrorAlert" class="alert alert-danger alert-dismissible fade show" role="alert" style="display: none;">
  <strong>❌ Đăng nhập Google thất bại</strong>
  <p id="oauth2ErrorMessage" class="mb-0 mt-2"></p>
  <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Đóng"></button>
</div>

<!-- Thông báo yêu cầu email trường -->
<div class="alert alert-info" role="alert">
  <strong>📧 Lưu ý:</strong> 
  Chỉ cho phép đăng nhập bằng email trường:
  <ul class="mb-0 mt-2">
    <li>Email giảng viên/nhân viên: <code>@hcmute.edu.vn</code></li>
    <li>Email sinh viên: <code>@student.hcmute.edu.vn</code></li>
  </ul>
</div>
```

**JavaScript - Xử lý error message:**
```javascript
// Xử lý OAuth2 error message từ URL
(function() {
  const urlParams = new URLSearchParams(window.location.search);
  const errorType = urlParams.get('error');
  const errorMessage = urlParams.get('message');
  
  if (errorType === 'oauth2' && errorMessage) {
    const alertDiv = document.getElementById('oauth2ErrorAlert');
    const messageDiv = document.getElementById('oauth2ErrorMessage');
    
    if (alertDiv && messageDiv) {
      // Decode error message
      const decodedMessage = decodeURIComponent(errorMessage);
      messageDiv.textContent = decodedMessage;
      alertDiv.style.display = 'block';
      
      // Scroll to alert
      alertDiv.scrollIntoView({ behavior: 'smooth', block: 'center' });
      
      // Clean URL (remove error params)
      const cleanUrl = window.location.pathname;
      window.history.replaceState({}, document.title, cleanUrl);
    }
  }
})();
```

---

## 🔄 Luồng hoạt động

### **Trường hợp 1: Email hợp lệ ✅**

```
User click "Đăng nhập bằng Google"
    ↓
Redirect to Google Login
    ↓
User login: nguyenvana@hcmute.edu.vn
    ↓
Google redirect về: /login/oauth2/code/google?code=xxx
    ↓
Spring Security exchange code → access token
    ↓
CustomOAuth2UserService.loadUser()
    ├─ Extract email: nguyenvana@hcmute.edu.vn
    ├─ Validate email_verified: ✅ true
    ├─ Validate domain: ✅ @hcmute.edu.vn
    └─ Create/update user in database
    ↓
OAuth2LoginSuccessHandler
    ├─ Create session
    └─ Redirect to /
    ↓
✅ Đăng nhập thành công
```

### **Trường hợp 2: Email không hợp lệ ❌**

```
User click "Đăng nhập bằng Google"
    ↓
Redirect to Google Login
    ↓
User login: user@gmail.com
    ↓
Google redirect về: /login/oauth2/code/google?code=xxx
    ↓
Spring Security exchange code → access token
    ↓
CustomOAuth2UserService.loadUser()
    ├─ Extract email: user@gmail.com
    ├─ Validate email_verified: ✅ true
    ├─ Validate domain: ❌ FAIL
    └─ throw OAuth2AuthenticationException(
          "Chỉ cho phép đăng nhập bằng email @hcmute.edu.vn hoặc @student.hcmute.edu.vn"
        )
    ↓
OAuth2LoginFailureHandler.onAuthenticationFailure()
    ├─ Extract error message
    ├─ Encode message
    └─ Redirect to /views/login?error=oauth2&message=...
    ↓
login.html
    ├─ JavaScript detect error params
    ├─ Show error alert
    └─ Display message: "Chỉ cho phép đăng nhập bằng email @hcmute.edu.vn..."
    ↓
❌ User thấy thông báo lỗi rõ ràng
```

---

## 🧪 Test Cases

### **Test 1: Email giảng viên hợp lệ**
```
Email: nguyenvana@hcmute.edu.vn
Expected: ✅ Đăng nhập thành công
Console: ✅ Email domain validated: nguyenvana@hcmute.edu.vn
```

### **Test 2: Email sinh viên hợp lệ**
```
Email: 21110123@student.hcmute.edu.vn
Expected: ✅ Đăng nhập thành công
Console: ✅ Email domain validated: 21110123@student.hcmute.edu.vn
```

### **Test 3: Email Gmail không hợp lệ**
```
Email: user@gmail.com
Expected: ❌ Redirect về /views/login với error message
UI: Alert đỏ hiển thị "Chỉ cho phép đăng nhập bằng email @hcmute.edu.vn hoặc @student.hcmute.edu.vn"
Console: ❌ OAuth2 login rejected: Invalid email domain - user@gmail.com
```

### **Test 4: Email Yahoo không hợp lệ**
```
Email: user@yahoo.com
Expected: ❌ Redirect về /views/login với error message
UI: Alert đỏ hiển thị thông báo lỗi
```

### **Test 5: Email subdomain khác**
```
Email: user@other.hcmute.edu.vn
Expected: ❌ Redirect về /views/login với error message
```

### **Test 6: Email chưa verify**
```
Email: user@hcmute.edu.vn (email_verified = false)
Expected: ❌ Redirect về /views/login
Error: "Email not verified by Google"
```

---

## 🎨 UI Screenshots

### **Trang login - Thông báo hướng dẫn:**
```
┌─────────────────────────────────────────────────────┐
│ 📧 Lưu ý:                                           │
│ Chỉ cho phép đăng nhập bằng email trường:          │
│ • Email giảng viên/nhân viên: @hcmute.edu.vn       │
│ • Email sinh viên: @student.hcmute.edu.vn          │
└─────────────────────────────────────────────────────┘
```

### **Khi login thất bại:**
```
┌─────────────────────────────────────────────────────┐
│ ❌ Đăng nhập Google thất bại                        │
│                                                      │
│ Chỉ cho phép đăng nhập bằng email @hcmute.edu.vn   │
│ hoặc @student.hcmute.edu.vn                         │
│                                                 [X]  │
└─────────────────────────────────────────────────────┘
```

---

## 🔒 Bảo mật

### **Ưu điểm:**
- ✅ Validate ở backend (không thể bypass)
- ✅ Case-insensitive (không phân biệt hoa thường)
- ✅ Trim whitespace tự động
- ✅ Validate email_verified từ Google
- ✅ Thông báo lỗi rõ ràng cho user
- ✅ Redirect về login page (không để user bị stuck)
- ✅ Clean URL sau khi hiển thị error (tránh share link lỗi)

### **Lưu ý:**
- ⚠️ User phải có email trường trong Google account
- ⚠️ Email cá nhân (Gmail, Yahoo) sẽ bị từ chối
- ⚠️ Cần thông báo rõ ràng cho user trước khi họ login

---

## 🚀 Cách test

### **1. Restart server**
```bash
mvn spring-boot:run
```

### **2. Test với email hợp lệ:**
1. Truy cập: `http://localhost:8080/views/login`
2. Click "Đăng nhập bằng Google"
3. Login với email: `nguyenvana@hcmute.edu.vn`
4. **Expected:** ✅ Đăng nhập thành công, redirect về trang chủ

### **3. Test với email không hợp lệ:**
1. Truy cập: `http://localhost:8080/views/login`
2. Click "Đăng nhập bằng Google"
3. Login với email: `user@gmail.com`
4. **Expected:** ❌ Redirect về `/views/login` với alert đỏ hiển thị lỗi

### **4. Kiểm tra console logs:**
```
✅ Email domain validated: nguyenvana@hcmute.edu.vn
❌ OAuth2 login rejected: Invalid email domain - user@gmail.com
❌ OAuth2 Login Failed: Chỉ cho phép đăng nhập bằng email @hcmute.edu.vn...
```

---

## 🔄 Mở rộng

### **Thêm domain khác:**
```java
boolean isValidDomain = emailLower.endsWith("@hcmute.edu.vn") || 
                        emailLower.endsWith("@student.hcmute.edu.vn") ||
                        emailLower.endsWith("@admin.hcmute.edu.vn");  // Thêm domain mới
```

### **Lấy từ config file:**
```properties
# application.properties
oauth2.allowed-domains=@hcmute.edu.vn,@student.hcmute.edu.vn
```

```java
@Value("${oauth2.allowed-domains}")
private String allowedDomains;

private boolean isValidDomain(String email) {
    String emailLower = email.toLowerCase().trim();
    String[] domains = allowedDomains.split(",");
    
    for (String domain : domains) {
        if (emailLower.endsWith(domain.trim())) {
            return true;
        }
    }
    return false;
}
```

---

## ✅ Kết luận

Hệ thống giờ đã:
- ✅ Giới hạn OAuth2 login chỉ cho email trường
- ✅ Redirect về login page khi thất bại
- ✅ Hiển thị thông báo lỗi rõ ràng
- ✅ Hướng dẫn user về yêu cầu email
- ✅ Bảo mật cao, không thể bypass
- ✅ UX tốt, user biết lý do thất bại

**Hệ thống sẵn sàng sử dụng!** 🎓