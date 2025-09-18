
import { useState } from "react";
import { useSelector, useDispatch } from "react-redux";

import style from "./HomeLeftPanel.module.css"
import TabSelector1 from "./TabSelector1"


const homeLeftPanelContents2 = [
  {"imgSrc": "./various-icon-1-market.png", "altText": "Marketplace", "contentText": "Marketplace"},
  {"imgSrc": "./various-icon-1-game.png", "altText": "Chơi game", "contentText": "Chơi game"},
  {"imgSrc": "./various-icon-2-messenger.png", "altText": "Messenger", "contentText": "Messenger"},
]
const HomeMoreTabSelector1 = () => {
  const [isExpanded, setIsExpanded] = useState(false);

  const toggleExpand = () => {
    setIsExpanded(!isExpanded);
  };

  return ( 
    <div>
      <div className={style[isExpanded ? "show" : "hiden"]}>
          {/* Các Tabs */}
          {homeLeftPanelContents2.map((value, index) => 
            <TabSelector1
              key={index}
              imgSrc={value["imgSrc"]}
              altText={value["altText"]}
              contentText={value["contentText"]}
            />
          )}
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

// =================================================
const homeLeftPanelContents1 = [
  {"imgSrc": "./user-avatar-1.png", "altText": "User Avatar", "contentText": "Trang cá nhân"},
  {"imgSrc": "./meta-ai-icon.png", "altText": "Meta AI", "contentText": "Meta AI"},
  {"imgSrc": "./various-icon-1-friends.png", "altText": "Bạn bè", "contentText": "Bạn bè"},
  {"imgSrc": "./various-icon-1-groups.png", "altText": "Nhóm", "contentText": "Nhóm"},
  {"imgSrc": "./various-icon-1-memories.png", "altText": "Kỷ niệm", "contentText": "Kỷ niệm"},
  {"imgSrc": "./various-icon-1-save.png", "altText": "Đã lưu", "contentText": "Đã lưu"},
  {"imgSrc": "./various-icon-1-video2.png", "altText": "Video", "contentText": "Video"},
]
const HomeLeftPanel = () => {

  const auth = useSelector((state) => state.auth);
  homeLeftPanelContents1[0]["contentText"] = auth.full_name;

  return (
    <div>
        <aside className={`${style["sidebar"]} ${style["left"]}`}>

          {/* Các Tabs */}
          {homeLeftPanelContents1.map((value, index) => 
            <TabSelector1
              key={index}
              imgSrc={value["imgSrc"]}
              altText={value["altText"]}
              contentText={value["contentText"]}
            />
          )}

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