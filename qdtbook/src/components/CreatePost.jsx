import React, { useState } from "react";

import "./CreatePost.css";
import CreatePostModal from "../popup-window/CreatePostModal";

export default function CreatePost() {
  const [isModalOpen, setIsModalOpen] = useState(false);

  return (
    <div className="create-post">
      <div className="create-post__top">
        <img
          src="/user-avatar-1.png"
          alt="User Avatar"
          className="create-post__avatar"
        />
        <input
          type="text"
          className="create-post__input"
          placeholder="Tên ơi, bạn đang nghĩ gì thế?"
          onClick={() => setIsModalOpen(true)}
        />
        <CreatePostModal
          isOpen={isModalOpen}
          onClose={() => setIsModalOpen(false)}
        />
      </div>

      <div className="create-post__divider"></div>

      <div className="create-post__actions">
        <button className="action-btn live">
          <img src="/camera-1.png" alt="Live" className="action-icon" />
          Video trực tiếp
        </button>
        <button className="action-btn photo">
          <img src="/media-icon-1.png" alt="Photo" className="action-icon" />
          Ảnh/video
        </button>
        <button className="action-btn feeling">
          <img src="/smile-face-1.png" alt="Feeling" className="action-icon" />
          Cảm xúc/hoạt động
        </button>
      </div>
    </div>
  );
}
