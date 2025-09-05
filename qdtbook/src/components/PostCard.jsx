import React from "react";
import "./PostCard.css";

const PostCard = ({ avatar, name, time, content, image, likes, comments, shares }) => {
  return (
    <div className="post-card">
      {/* Header */}
      <div className="post-header">
        <img src={avatar == null ? '/user-avatar-1.png' : avatar} alt="avatar" className="post-avatar" />
        <div className="post-info">
          <h4 className="post-name">{name}</h4>
          <span className="post-time">{time}</span>
        </div>
        <button className="post-menu">⋯</button>
      </div>

      {/* Content */}
      <div className="post-content">
        <p>{content}</p>
        {/* {image || <img src={image == null ? '/user-avatar-1.png' : image} alt="post" className="post-image" />} */}
        {image || <img src={image == null ? '/user-avatar-1.png' : image} alt="post" className="post-image" />}
      </div>

      {/* Footer */}
      <div className="post-footer">
        <div className="post-reactions">
          👍😆❤️ {likes}
        </div>
        <div className="post-stats">
          <span className="comments-link">{comments} bình luận</span>
          <span className="shares-link">{shares} lượt chia sẻ</span>
        </div>
      </div>

      <div className="black-line"></div>

      <div className="post-actions">
        <button className="action-btn"> 
          <img src="./like-1.png" alt="like" className="post-action-icon"/>
          Thích
        </button>
        <button className="action-btn">
          <img src="./comment-1.png" alt="like" className="post-action-icon"/>
          Bình luận
        </button>
        <button className="action-btn">
          <img src="./share-1.png" alt="like" className="post-action-icon"/>
          Chia sẻ
        </button>
      </div>  

    </div>
  );
};

export default PostCard;
