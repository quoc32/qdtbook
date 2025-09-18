import React, { useState, useRef, useEffect } from "react";
import { useNavigate } from "react-router-dom";

import style from "./AvatarMenuModal.module.css";


const TopPanel = () => {

  return (
    <div className={"rounded-3 m-2 p-1"} style={{ maxWidth: "100%", boxShadow: "0 2px 6px rgba(0, 0, 0, 0.2)" }}>
      <div className="text-center p-0">
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
        <div className="btn-light d-flex align-items-center justify-content-center m-1 p-0 py-1 gap-2 auto-darken"
          style={{maxWidth: '100%', backgroundColor: "#dce2e9ff", borderRadius: "8px", cursor: "pointer"}}
        >
          <div className={style.avatarWrapper2}>
            <img src={"group-users-2.png"} alt={"Ảnh quoccute"} className={style.avatarWrapper2Image} />
          </div>
          <i className="bi bi-people m-0"></i>
          <span style={{fontSize: "13px"}}>Xem tất cả trang cá nhân</span>
        </div>
      </div>
    </div>
  );
}

const SelectionButton = ({ avatar, name, handleClick, have_arrow = false}) => {
  return (
    <div className={style.contact} onClick={handleClick}>
      <div className={style.avatarWrapper}>
        <img src={avatar} alt={name} className={style.avatar} />
      </div>
      <span className={style.name}>{name}</span>
      {have_arrow && 
      <div className="ms-auto d-flex center" style={{width: '12px', height: '12px'}}
      >
        <img src="arrow-left-1.png" alt="arrow"/></div>}
    </div>
  );
}

// >> Main
const AvatarMenuModal = ({ref}) => {
  const navigate = useNavigate();

  // Hàm sử lý khi Đăng xuất
  const handleLogout = async () => {
    try {
      // gọi API logout
      await fetch(import.meta.env.VITE_API_URL + "/auth/logout", {
        method: "POST",
        credentials: "include", // cần gửi cookie JSESSIONID
      });

      // xóa dữ liệu client-side
      localStorage.removeItem("sessionId");
      sessionStorage.removeItem("auth");

      // điều hướng về login
      navigate("/login");
    } catch (error) {
      console.error("Lỗi khi logout:", error);
      // fallback: vẫn đưa user về login
      // navigate("/login");
    }
  };

  return (
    <div
      ref={ref}
      className={"position-fixed p-0 m-0 shadow rounded-3 " + style["avatar-menu-modal"]}
      style={{zIndex: 9500 , right: '2%', top: '8%', width: '230px'}}
    >
      <TopPanel />

      <SelectionButton avatar={"icons-bundle-3-setting.png"} name={"Cài đặt và quyền riêng tư"} have_arrow/>
      <SelectionButton avatar={"icon-question-mark-1.png"} name={"Trợ giúp và hỗ trợ"} have_arrow/>
      <SelectionButton avatar={"moon-1.png"} name={"Màn hình và trợ năng"} have_arrow/>
      <SelectionButton avatar={"message-box-1.png"} name={"Đóng góp ý kiến"}/>
      <SelectionButton avatar={"door-1.png"} name={"Đăng xuất"} handleClick={handleLogout}/>

      <div className="mb-2"></div>

    </div>
  );
};

export default AvatarMenuModal;
