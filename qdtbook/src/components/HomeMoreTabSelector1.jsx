
import { useState } from "react";

import "./HomeMoreTabSelector1.css"
import TabSelector1 from "./TabSelector1"

const HomeMoreTabSelector1 = () => {
  const [isExpanded, setIsExpanded] = useState(false);

  const toggleExpand = () => {
    setIsExpanded(!isExpanded);
  };

  return ( 
    <div>

      <div className={isExpanded ? "show" : "hiden"}>
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

      <div className="expan-sidebar-item"
        onClick={toggleExpand}
      >
        <img
          src={isExpanded ? "./arrow-up-1.png" : "./arrow-down-1.png"}
          alt="Expand icon"
          className="sidebar-item__avatar"
        />
        <span className="sidebar-item__label">{isExpanded ? "Ẩn bớt" : "Xem thêm"}</span>
      </div>
    </div>
  );
}
 
export default HomeMoreTabSelector1;