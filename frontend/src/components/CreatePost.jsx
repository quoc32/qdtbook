import React, { useState } from "react";
import { useSelector, useDispatch } from "react-redux";

import style from "./CreatePost.module.css";
import CreatePostModal from "../popup/CreatePostModal";

// Caption
const CreatePostCaption = ({setIsModalOpen, isModalOpen}) => {
  const auth = useSelector((state) => state.auth);

  return (
    <div className={style["create-post__top"]}>
      <img
        src="/user-avatar-1.png"
        alt="User Avatar"
        className={style["create-post__avatar"]}
      />
      <input
        type="text"
        className={style["create-post__input"]}
        placeholder={`${auth.full_name} ơi, bạn đang nghĩ gì thế?`}
        onClick={() => setIsModalOpen(true)}
      />
      <CreatePostModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
      />
    </div>
  )
}

export default function CreatePost() {
  const [isModalOpen, setIsModalOpen] = useState(false);

  return (
    <div className={style["create-post"]}>
      <CreatePostCaption setIsModalOpen={setIsModalOpen} isModalOpen={isModalOpen}/>

      <div className={style["create-post__divider"]}></div>

      <div className={style["create-post__actions"]}>
        {/* Video trực tiếp */}
        <button className={`${style["action-btn"]} ${style["live"]}`}>
          <img src="/camera-1.png" alt="Live" className={style["action-icon"]} />
          Video trực tiếp
        </button>

        {/* Ảnh/Video */}
        <button className={`${style["action-btn"]} ${style["photo"]}`}>
          <img src="/media-icon-1.png" alt="Photo" className={style["action-icon"]} />
          Ảnh/video
        </button>
        
        {/* Cảm xúc/hoạt động */}
        <button className={`${style["action-btn"]} ${style["feeling"]}`}>
          <img src="/smile-face-1.png" alt="Feeling" className={style["action-icon"]} />
          Cảm xúc/hoạt động
        </button>
        
      </div>

    </div>
  );
}
