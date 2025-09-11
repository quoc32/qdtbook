
import { useState } from "react";

import style from "./HomeLeftPanel.module.css"
import TabSelector1 from "./TabSelector1"

const HomeMoreTabSelector1 = () => {
  const [isExpanded, setIsExpanded] = useState(false);

  const toggleExpand = () => {
    setIsExpanded(!isExpanded);
  };

  return ( 
    <div>

      <div className={style[isExpanded ? "show" : "hiden"]}>
        {/* Marketplace */}
        <TabSelector1
          imgSrc="./various-icon-1-market.png"
          altText="Marketpalce icon"
          contentText="Marketplace"
        />
        {/* Game */}
        <TabSelector1
          imgSrc="./various-icon-1-game.png"
          altText="Game icon"
          contentText="Chơi game"
        />
        {/* Messenger */}
        <TabSelector1
          imgSrc="./various-icon-2-messenger.png"
          altText="Messenger icon"
          contentText="Messenger"
        />
      </div>

      <div className={style["expan-sidebar-item"]}
        onClick={toggleExpand}
      >
        <img
          src={isExpanded ? "./arrow-up-1.png" : "./arrow-down-1.png"}
          alt="Expand icon"
          className={style["sidebar-item__avatar"]}
        />
        <span className={style["sidebar-item__label"]}>{isExpanded ? "Ẩn bớt" : "Xem thêm"}</span>
      </div>

    </div>
  );
}


const HomeLeftPanel = () => {
  return (
    <div>
      {/* Cột trái */}
        <aside className={`${style["sidebar"]} ${style["left"]}`}>
          {/* Tang cá nhân */}
          <TabSelector1
            imgSrc="./user-avatar-1.png"
            altText="User Avatar"
            contentText="Trang cá nhân"
          />
          {/* Meta AI */}
          <TabSelector1
            imgSrc="./meta-ai-icon.png"
            altText="Meta AI"
            contentText="Meta AI"
          />
          {/* Friends */}
          <TabSelector1
            imgSrc="./various-icon-1-friends.png"
            altText="Friends icon"
            contentText="Bạn bè"
          />
          {/* Groups */}
          <TabSelector1
            imgSrc="./various-icon-1-groups.png"
            altText="Groups icon"
            contentText="Nhóm"
          />
          {/* Groups */}
          <TabSelector1
            imgSrc="./various-icon-1-memories.png"
            altText="Memories icon"
            contentText="Kỷ niệm"
          />
          {/* Save */}
          <TabSelector1
            imgSrc="./various-icon-1-save.png"
            altText="Save icon"
            contentText="Đã lưu"
          />
          {/* Video */}
          <TabSelector1
            imgSrc="./various-icon-1-video2.png"
            altText="Video icon"
            contentText="Video"
          />
          {/* Xem thêm */}
          <HomeMoreTabSelector1 />
  
          <div className={style["black-line"]}></div>
          <span style={{ fontWeight: 600, color: "#65676b", fontSize: 12, marginTop: 0, marginBottom: 10 }}>
            Lối tắt của bạn
          </span>
  
          
        </aside>
    </div>
  );
}
 
export default HomeLeftPanel;