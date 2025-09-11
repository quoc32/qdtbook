import { Link } from "react-router-dom";
import { useState } from "react";

import style from "./NavBar.module.css";
import homeIcon from "../assets/home-icon.png";
import friendsIcon from "../assets/friends-icon.png";
import videoIcon from "../assets/video-icon.png";
import marketIcon from "../assets/market-icon.png";
import gameIcon from "../assets/game-icon.png";
import fbLogo from "../assets/fb-logo.ico";

// bên phải
import menuIcon from "../assets/menu.png";
import messengerIcon from "../assets/messenger-logo.png";
import bellIcon from "../assets/bell.png";

const NavBarItem = ({ icon, altText, link }) => (
  <li>
    <Link to={link}>
      <img src={icon} alt={altText} className={style["nav-icon"]} />
    </Link>
  </li>
);

const NavBar = () => {


  return (
    <nav className={style["navbar"]}>
      {/* Bên trái: logo + search */}
      <Link to="/" style={{ textDecoration: "none" }}>
        <div className={style["nav-left"]}>
          <img src={fbLogo} alt="Facebook" className={style["logo"]} />
          <input type="text" placeholder="Tìm kiếm trên Facebook" className={style["search-input"]} />
        </div>
      </Link>

      {/* Giữa: các icon menu */}
      <ul className={style["nav-list"]}>
        <NavBarItem icon={homeIcon} altText="Home" link="/" />
        <NavBarItem icon={friendsIcon} altText="Friends" link="/friends" />
        <NavBarItem icon={videoIcon} altText="Watch" link="/videos" />
        <NavBarItem icon={marketIcon} altText="Marketplace" link="/marketplace" />
        <NavBarItem icon={gameIcon} altText="Games" link="/games" />
      </ul>

      {/* Bên phải: menu + messenger + bell + avatar */}
      <div className={style["nav-right"]}>
        <img src={menuIcon} alt="Menu" className={style["right-icon"]} />
        <img src={messengerIcon} alt="Messenger" className={style["right-icon"]} />
        <img src={bellIcon} alt="Notifications" className={style["right-icon"]} />
        <img src="/user-avatar-1.png" alt="Avatar" className={style["avatar"]} />
      </div>
    </nav>
  );
};
 
export default NavBar;