import { NavLink, Link } from "react-router-dom";
import { useState, useEffect, useRef } from "react";

import style from "./NavBar.module.css";
import homeIcon from "../assets/home-icon.png";
import friendsIcon from "../assets/friends-icon.png";
import videoIcon from "../assets/video-icon.png";
import marketIcon from "../assets/market-icon.png";
import gameIcon from "../assets/game-icon.png";
import fbLogo from "../assets/fb-logo.ico";
import TooltipWapper from "./TooltipWapper";

// bên phải
import menuIcon from "../assets/menu.png";
import messengerIcon from "../assets/messenger-logo.png";
import bellIcon from "../assets/bell.png";

// Modal
import AvatarMenuModal from "../popup/AvatarMenuModal";
import NotificationMenuModal from "../popup/NotificationMenuModal";
import MenuModal from "../popup/MenuModal";

const NavBarItem = ({ icon, altText, link }) => (
  <li>
    <NavLink
      to={link}
      className={({ isActive }) => 
        isActive ? `${style["nav-link"]} ${style["active"]}` : style["nav-link"]
      }
    >
      <img src={icon} alt={altText} className={style["nav-icon"]} />
      {/* underline chỉ hiển thị khi active */}
      <div className={style["underline"]}></div>
    </NavLink>
  </li>
);


const NavBar = () => {
  
  // >> Xử lý đóng mở modal của avatar
  const [isAvatarModalOpen, setIsAvatarModalOpen] = useState(false);
  const modalAvatarRef = useRef(null);
  const avatarClickHandler = () => {
    setIsAvatarModalOpen(true);
  };
  useEffect(() => {
    const handleClickOutside = (event) => {
      // nếu modal đang mở và click KHÔNG nằm trong modalAvatarRef
      if (isAvatarModalOpen && modalAvatarRef.current 
        && !modalAvatarRef.current.contains(event.target) 
        && (event.target.alt !== "Avatar")) 
      {
        setIsAvatarModalOpen(false);
        console.log("Đóng modal avatar");
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [isAvatarModalOpen]);

  // >> Xử lý đóng mở modal của notification
  const [isNotificationModalOpen, setIsNotificationModalOpen] = useState(false);
  const modalNotificationRef = useRef(null);
  const notificationClickHandler = () => {
    setIsNotificationModalOpen(true);
  };
  useEffect(() => {
    const handleClickOutside = (event) => {
      // nếu modal đang mở và click KHÔNG nằm trong modalNotificationRef
      if (isNotificationModalOpen && modalNotificationRef.current 
        && !modalNotificationRef.current.contains(event.target) 
        && (event.target.alt !== "Notifications")) 
      {
        setIsNotificationModalOpen(false);
        console.log("Đóng modal notification");
      }
    };
    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [isNotificationModalOpen]);
  
  // >> Xử lý đóng mở modal của Menu
  const [isMenuModalOpen, setIsMenuModalOpen] = useState(false);
  const modalMenuRef = useRef(null);
  const menuClickHandler = () => {
    setIsMenuModalOpen(true);
  };
  useEffect(() => {
    const handleClickOutside = (event) => {
      // nếu modal đang mở và click KHÔNG nằm trong modalMenuRef
      if (isMenuModalOpen && modalMenuRef.current 
        && !modalMenuRef.current.contains(event.target) 
        && (event.target.alt !== "Menu")) 
      {
        setIsMenuModalOpen(false);
        console.log("Đóng modal menu");
      }
    };
    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [isMenuModalOpen]);

  return (
    <nav className={style["navbar"]}>
      {/* Bên trái: logo + search */}
      <Link to="/" style={{ textDecoration: "none" }} tabIndex="-1">
        <div className={style["nav-left"]}>
          <img src={fbLogo} alt="Facebook" className={style["logo"]} />
          <input type="text" placeholder="Tìm kiếm trên Facebook" className={style["search-input"]} />
        </div>
      </Link>

      {/* Giữa: các icon menu */}
      <ul className={style["nav-list"]}>
        <TooltipWapper tooltipText={"Trang chủ"}><NavBarItem icon={homeIcon} altText="Home" link="/" /></TooltipWapper>
        <TooltipWapper tooltipText={"Bạn bè"}><NavBarItem icon={friendsIcon} altText="Friends" link="/friends" /></TooltipWapper>
        <TooltipWapper tooltipText={"Video"}><NavBarItem icon={videoIcon} altText="Watch" link="/videos" /></TooltipWapper>
        <TooltipWapper tooltipText={"Marketplace"}><NavBarItem icon={marketIcon} altText="Marketplace" link="/marketplace" /></TooltipWapper>
        <TooltipWapper tooltipText={"Trò chơi"}><NavBarItem icon={gameIcon} altText="Games" link="/games" /></TooltipWapper>
      </ul>

      {/* Bên phải: menu + messenger + bell + avatar */}
      <div className={style["nav-right"]}>
        <TooltipWapper tooltipText={"Menu"}>
          <div className={isMenuModalOpen ? `${style["active"]} ${style["active-right-icon-wapper"]}` : ""}>
            <img src={menuIcon} alt="Menu" 
            className={style["right-icon"]} 
            onClick={menuClickHandler}/>
          </div>
        </TooltipWapper>
        
        <TooltipWapper tooltipText={"Messenger"}>
          <img src={messengerIcon} alt="Messenger" className={style["right-icon"]} />
        </TooltipWapper>

       <TooltipWapper tooltipText={"Thông báo"}>
          <div className={isNotificationModalOpen ? `${style["active"]} ${style["active-right-icon-wapper"]}` : ""}>
           <img src={bellIcon} alt="Notifications" 
              className={style["right-icon"]} 
              onClick={notificationClickHandler}/>
          </div>
       </TooltipWapper>

        <TooltipWapper tooltipText={"Tài khoản"} additionalStyle={{ left: "0px"}}>
          <img src="/user-avatar-1.png" alt="Avatar" 
            className={style["avatar"]} 
            onClick={avatarClickHandler}/>
        </TooltipWapper>

      </div>

      {/* Modal */}
      {isAvatarModalOpen && (
       <AvatarMenuModal ref={modalAvatarRef}/>
      )}
      {isNotificationModalOpen && (
       <NotificationMenuModal ref={modalNotificationRef} isNotificationModalOpen={isNotificationModalOpen}/>
      )}
      {isMenuModalOpen && (
       <MenuModal ref={modalMenuRef} isMenuModalOpen={isMenuModalOpen}/>
      )}


    </nav>
  );
};
 
export default NavBar;