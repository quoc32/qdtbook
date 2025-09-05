import { useState } from "react";

import "./CreatePostModal.css";
import { createPost } from "../utils/CreatePost";

export default function CreatePostModal({ isOpen, onClose }) {
  const [postContent, setPostContent] = useState("");
  const [loading, setLoading] = useState(false);
  
  if (!isOpen) return null;

  const handleSubmit = async () => {
    if (!postContent.trim()) return;

    setLoading(true);
    createPost(postContent)
      .then((data) => {
        alert("Đăng thành công: " + JSON.stringify(data));
      })
      .catch((err) => {
        alert("Có lỗi: " + err.message);
      });
  };

  return (
    <div className="modal-overlay">
      <div className="modal">
        {/* Header */}
        <div className="modal-header">
          <h2>Tạo bài viết</h2>
          <button className="close-btn" onClick={() => {setPostContent(""); onClose()}}>✕</button>
        </div>

        {/* User info */}
        <div className="modal-user">
          <img src="/user-avatar-1.png" alt="Avatar" className="user-avatar" />
          <div>
            <div className="user-name">Họ Tên</div>
            <button className="privacy-btn">👥 Bạn bè ▾</button>
          </div>
        </div>

        {/* Input */}
        <textarea
          className="post-input"
          placeholder="Tên ơi, bạn đang nghĩ gì thế?"
          value={postContent}
          onChange={(e) => setPostContent(e.target.value)}
        ></textarea>

        {/* Add to post */}
        <div className="add-section">
          <span>Thêm vào bài viết của bạn</span>
          <div className="add-icons">
            <button><img src="/photo.png" alt="Ảnh" /></button>
            <button><img src="/tag.png" alt="Tag" /></button>
            <button><img src="/feeling.png" alt="Cảm xúc" /></button>
            <button><img src="/location.png" alt="Địa điểm" /></button>
            <button><img src="/gif.png" alt="GIF" /></button>
            <button><img src="/more.png" alt="Thêm" /></button>
          </div>
        </div>

        {/* Post button */}
        <button
          className="post-btn"
          onClick={handleSubmit}
          disabled={!postContent || loading}
        >
          {loading ? "Đang đăng..." : "Đăng"}
        </button>
      
      </div>
    </div>
  );
}
