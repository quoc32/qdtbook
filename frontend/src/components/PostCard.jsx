import React from "react";
import style from "./PostCard.module.css";

const PostCard = ({ avatar, name, time, content, image, likes, comments, shares }) => {
  return (
    <div className={style["post-card"]}>
      {/* Header */}
      <div className={style["post-header"]}>
        <img src={avatar == null ? '/user-avatar-1.png' : avatar} alt="avatar" className={style["post-avatar"]} />
        <div className={style["post-info"]}>
          <h4 className={style["post-name"]}>{name}</h4>
          <span className={style["post-time"]}>{time}</span>
        </div>
        <button className={style["post-menu"]}>⋯</button>
        <button className={style["post-menu"]}>x</button>
      </div>

      {/* Content */}
      <div className={style["post-content"]}>
        <p>{content}</p>
        {/* {image || <img src={image == null ? '/user-avatar-1.png' : image} alt="post" className={style["post-image" />} */}
        {image || <img src={image == null ? '/user-avatar-1.png' : image} alt="post" className={style["post-image"]} />}
      </div>

      {/* Footer */}
      <div className={style["post-footer"]}>
        <div className={style["post-reactions"]}>
          👍😆❤️ {likes}
        </div>
        <div className={style["post-stats"]}>
          <span className={style["comments-link"]}>{comments} bình luận</span>
          <span className={style["shares-link"]}>{shares} lượt chia sẻ</span>
        </div>
      </div>

      <div className={style["black-line"]}></div>

      <div className={style["post-actions"]}>
        <button className={style["action-btn"]}> 
          <img src="./like-1.png" alt="like" className={style["post-action-icon"]}/>
          Thích
        </button>
        <button className={style["action-btn"]}>
          <img src="./comment-1.png" alt="like" className={style["post-action-icon"]}/>
          Bình luận
        </button>
        <button className={style["action-btn"]}>
          <img src="./share-1.png" alt="like" className={style["post-action-icon"]}/>
          Chia sẻ
        </button>
      </div>  

    </div>
  );
};

export default PostCard;
