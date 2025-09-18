import React, { useState, useRef, useEffect } from "react";

import style from "./NotificationMenuModal.module.css";
import SimpleButton1 from "../components/SimpleButton1";

const HeaderBar = () => {
  return (
    <div>
      <div className={"d-flex align-items-center justify-content-between p-0 pt-2 ps-3 pe-3 " + style["headerBar-container"]}>
        <h6 style={{fontWeight: 600}}>Thông báo</h6>
        <SimpleButton1 imageSrc={"three-dot-1.png"} imageAlt={"3 dot image"} noTooltip/>
      </div>      
    </div>
  );
}

const SelectionBar = () => {
  const [selected, setSelected] = useState("all");

  return (
    <div className={style["selectionBar-container"]}>
      <button
        className={`${style["selectionBar-option"]} ${
          selected === "all" ? style["selectionBar-active"] : ""
        }`}
        onClick={() => setSelected("all")}
      >
        Tất cả
      </button>
      <button
        className={`${style["selectionBar-option"]} ${
          selected === "unread" ? style["selectionBar-active"] : ""
        }`}
        onClick={() => setSelected("unread")}
      >
        Chưa đọc
      </button>
    </div>
  );
};

const NotificationItem = ({ 
  avatar, 
  icon, 
  content, 
  time, 
  onJoin, 
  onDelete
}) => {
  return (
    <div className={style["NotificationItem-item"]}>
      {/* Bên trái: avatar + icon overlay */}
      <div className={style["NotificationItem-avatarWrapper"]}>
        <img src={avatar} alt="avatar" className={style["NotificationItem-avatar"]} />
        {icon && <img src={icon} alt="icon" className={style["NotificationItem-icon"]} />}
      </div>

      {/* Bên phải: nội dung */}
      <div className={style["NotificationItem-content"]}>
        <p className={style["NotificationItem-text"]} dangerouslySetInnerHTML={{ __html: content }} />
        <span className={style["NotificationItem-time"]}>{time}</span>

        <div className={style["NotificationItem-actions"]}>
          <button className={style["NotificationItem-joinBtn"]} onClick={onJoin}>Tham gia</button>
          <button className={style["NotificationItem-deleteBtn"]} onClick={onDelete}>Xóa</button>
        </div>
      </div>
    </div>
  );
};


const NotificationMenuModal = ({ref}) => {

  return (
    <div
      ref={ref}
      className={"position-fixed p-0 m-0 shadow rounded-3 " + style["avatar-menu-modal"]}
      style={{zIndex: 1050 , right: '2%', top: '8%', width: '230px'}}
    >
      <HeaderBar />
      <SelectionBar />
      
      {/* Thanh điều hướng khác */}
      <div className={"d-flex align-items-center justify-content-between pt-1"}>
        <div
          className={"px-2"}
          style={{ fontSize: '11px', textDecoration: 'none', fontWeight: '500' }}
        >
          Trước đó
        </div>
        <a className="link-offset-2 link-underline link-underline-opacity-0 px-2 m-0"
          href="#"
          style={{ fontSize: '10px', textDecoration: 'none', fontWeight: '400' }}
        >
          Xem tất cả
        </a>
      </div>
      {/* ========================= */}
      <NotificationItem 
        avatar={"user-avatar-1.png"}
        icon={"various-icon-1-groups.png"}
        content={"<b>Nguyễn Văn A</b> đã thêm bạn vào nhóm <b>Học lập trình cùng nhau</b>."}
        time={"1 giờ"}
        onJoin={() => console.log("Tham gia nhóm")}
        onDelete={() => console.log("Xóa thông báo")}
      />


    </div>
  );
};

export default NotificationMenuModal;
