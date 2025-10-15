USE spkt_connect;

-- ================================
-- Bảng Products (phiên bản hoàn chỉnh)
-- ================================
CREATE TABLE IF NOT EXISTS Products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    seller_id INT NOT NULL,                      -- Người đăng bán (Users.user_id)
    product_name VARCHAR(150) NOT NULL,          -- Tên sản phẩm
    description TEXT,                            -- Mô tả sản phẩm
    price DECIMAL(12,2) NOT NULL,                -- Giá bán
    quantity INT DEFAULT 1,                      -- Số lượng tồn
    status ENUM('available', 'out_of_stock', 'hidden') DEFAULT 'available', -- Trạng thái hàng
    address VARCHAR(255),                        -- Địa chỉ hoặc nơi giao hàng
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (seller_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    INDEX(seller_id)
);

-- ================================
-- Bảng Product_Media (giữ nguyên)
-- ================================
CREATE TABLE IF NOT EXISTS Product_Media (
    media_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    media_url VARCHAR(255) NOT NULL,             -- Đường dẫn ảnh/video
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES Products(product_id) ON DELETE CASCADE
);

-- ================================
-- Mở rộng bảng Notifications (thêm loại "product_share")
-- ================================
ALTER TABLE Notifications 
  MODIFY COLUMN `type` ENUM(
    'friend_request', 
    'friend_accept', 
    'post_reaction', 
    'post_comment', 
    'comment_reply', 
    'post_approved', 
    'important_post', 
    'post_share',
    'product_share'
  ) NOT NULL;
