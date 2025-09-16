import React, { useState, useRef, useEffect } from "react";

import style from "./MenuModal.module.css";
import SimpleButton1 from "../components/SimpleButton1";


const CreatePanelTabContents = [
  {imgURL: "/dang.png", text: "Đăng"},
  {imgURL: "/tin.png", text: "Tin"},
  {imgURL: "/thuoc-phim.png", text: "Thước phim"},
  {imgURL: "/su-kien-trong-doi.png", text: "Sự kiện trong đời"},
  {blackline: true},
  {imgURL: "/trang.png", text: "Trang"},
  {imgURL: "/quang-cao.png", text: "Quảng cáo"},
  {imgURL: "/nhom.png", text: "Nhóm"},
  {imgURL: "/su-kien.png", text: "Sự kiện"},
  {imgURL: "/market-2.png", text: "Bài niêm yết trên Marketplace"}
]

const CreatePanelTab = ({imgURL, text, blackline}) => {
  if (blackline) {
    return (
      <div style={{height: "1px", backgroundColor: "#a8a6a6ff", margin: "10px 15px"}}></div>
    )
  }
 
  return ( 
    <div className={style["createPaneTab-container"] + " d-flex flex-row"}>
      <div><img src={imgURL} alt={text} /></div>
      <span>{text}</span>
    </div>
  );
}

const CreatePanel = () => {
  return (
    <div className={style["createPanel"]}>
      <h6>Tạo</h6>
      {CreatePanelTabContents.map((element, idx) => (
        <CreatePanelTab key={idx} imgURL={element.imgURL} text={element.text} blackline={element.blackline}/>
      ))}
    </div>
  );
}

// >> =========Search Panel=========
const SearchPanelSectionTabContents = {
  "Xã hội": [
    {"imgURL" : "user-avatar-1.png", "title": "Sự kiện", "description": "Tổ chức hoặc tìm sự kiện cùng những hoạt động khác ở trên mạng ở quanh đây"},
    {"imgURL" : "user-avatar-1.png", "title": "Bạn bè", "description": "Tìm kiếm bạn bè hoặc những người bạn có thể biến"},
    {"imgURL" : "user-avatar-1.png", "title": "Nhóm", "description": "Kết nối những người cùng chung sở thích"},
    {"imgURL" : "user-avatar-1.png", "title": "Bảng tin", "description": "Xem bài viết phù hợp của những người và Trang bạn theo dõi"},
    {"imgURL" : "user-avatar-1.png", "title": "Bảng feed", "description": "Xem bài viết gần đây nhất từ bạn bè, nhóm, Trang và hơn thế nữa"},
    {"imgURL" : "user-avatar-1.png", "title": "Trang", "description": "Khám phá và kết nối doanh nghiệp trên QDTbook"},
  ],
  "Giải trí": [
    {"imgURL": "user-avatar-1.png", "title": "Video chơi game", "description": "Xem, kết nối với những game và người phát trực tiếp mà bạn yêu thích."},
    {"imgURL": "user-avatar-1.png", "title": "Chơi game", "description": "Chơi game bạn yêu thích."},
    {"imgURL": "user-avatar-1.png", "title": "Video", "description": "Đích đến của thước phim phù hợp với sở thích và quan hệ kết nối của bạn."}
  ],
  "Mua sắm": [
    {"imgURL": "user-avatar-1.png", "title": "Đơn đặt hàng và thanh toán", "description": "Một cách dễ dàng, bảo mật để thanh toán trên các ứng dụng bạn đang dùng."},
    {"imgURL": "user-avatar-1.png", "title": "Marketplace", "description": "Mua bán trong cộng đồng của bạn."}
  ],
  "Cá nhân": [
    {"imgURL": "user-avatar-1.png", "title": "Quảng cáo gần đây", "description": "Xem các quảng cáo bạn đã tương tác trên QDTbook."},
    {"imgURL": "user-avatar-1.png", "title": "Kỷ niệm", "description": "Ôn lại những khoảnh khắc và kỷ niệm trong quá khứ của bạn."},
    {"imgURL": "user-avatar-1.png", "title": "Đã lưu", "description": "Tìm lại bài viết, ảnh và video bạn đã lưu để xem sau."},
  ],
  "Các sản phẩm khác của QDT": [],
}
const SearchPanelSectionTab = ({ item }) => {
  return (
    <div className={style["searchPanelSectionTab-container"]}>
      <div><img src={item.imgURL} alt={item.title} /></div>
      <span>{item.title}</span>
      <p>{item.description}</p>
    </div>
  )
}

const SearchPanelSection = ({ category, items }) => {
  return (
    <div className="searchPanelSection-container">
      <div className="searchPanelSection-header">{category}</div>
      <div>
        {items.map((item, index) => (
          <SearchPanelSectionTab key={index} item={item} />
        ))}
      </div>
    </div>
  )
}

const SearchPanel = () => {
  return (
    <div className={style["search-panel-container"]}>
      <div className={style["search-bar"]}>
        {/* <span className={style["search-icon"]}>🔍</span> */}
        <input
          type="text"
          placeholder="Tìm kiếm trong menu"
          className={style["search-input"]}
        />
      </div>

      {Object.entries(SearchPanelSectionTabContents).map(([category, items]) => (
        <SearchPanelSection category={category} items={items}/>
      ))}

    </div>
  );
}
// >>===============================
const MenuModal = ({ref}) => {

  return (
    <div
      ref={ref}
      className={"position-fixed p-2 pt-1 m-0 shadow rounded-3 " + style["menu-modal"]}
        style={{
          zIndex: 1050,
          right: '2%',
          top: '8%',
          width: '400px',
          backgroundColor: "#F8F9FB",
      }}
    >
      <h6 style={{fontWeight: 700}}>Menu</h6>
      <div style={{
        height: '300px',
        overflowY: 'auto',
      }}>
        
        <div className={"d-flex justify-content-between shawdow"} style={{borderRadius: '5px'}}>
          <SearchPanel />
          <CreatePanel />
        </div>

      </div>
    </div>
  );
};

export default MenuModal;
