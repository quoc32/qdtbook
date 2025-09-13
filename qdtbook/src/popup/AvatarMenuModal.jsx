import React, { useState, useRef, useEffect } from "react";

import style from "./AvatarMenuModal.module.css";

const TopPanel = () => {
  return (
    <div className="card shadow-sm rounded-3 m-1 p-1" style={{ maxWidth: "100%", padding: 0 }}>
      <div className="card-body text-center p-0">
        {/* Avatar + Tên */}
        <div className="d-flex align-items-center m-0" 
          style={{fontSize: "12px"}}
        >
          <img
            src="/user-avatar-1.png"
            alt="avatar"
            className="rounded-circle me-2"
            style={{fontSize: "12px", width: "30px", height: "30px"}}
            width="40"
            height="40"
          />
          <h6 className="m-0 fw-bold" style={{fontSize: "12px"}}>Họ Tên</h6>
        </div>

        {/* Gạch ngang */}
        <hr className="p-0 m-1" />

        {/* Nút xem trang cá nhân */}
        <div className="btn btn-light d-flex align-items-center justify-content-center m-1 p-0"
          style={{maxWidth: '100%'}}
        >
          <i className="bi bi-people me-2"></i>
          <span style={{fontSize: "13px"}}>Xem tất cả trang cá nhân</span>
        </div>
      </div>
    </div>
  );
}

const SelectionButton = ({ avatar, name}) => {
  return (
    <div className={style.contact}>
      <div className={style.avatarWrapper}>
        <img src={avatar} alt={name} className={style.avatar} />
      </div>
      <span className={style.name}>{name}</span>
    </div>
  );
}

const AvatarMenuModal = () => {

  return (
    <div
      className={"position-fixed p-0 m-0 bg-primary "+ style["avatar-menu-modal"]}
      style={{zIndex: 1050 , right: '3%', top: '8%', width: '230px'}}
    >
      <TopPanel />

      <SelectionButton avatar={"user-avatar-1.png"} name={"quoc"}/>

    </div>
  );
};

export default AvatarMenuModal;
