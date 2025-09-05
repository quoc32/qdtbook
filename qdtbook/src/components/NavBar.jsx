import { Link } from "react-router-dom";

import "./NavBar.css";
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

const NavBar = () => {
  return (
    <nav className="navbar">
      {/* Bên trái: logo + search */}
      <Link to="/" style={{ textDecoration: "none" }}>
        <div className="nav-left">
          <img src={fbLogo} alt="Facebook" className="logo" />
          <input type="text" placeholder="Tìm kiếm trên Facebook" className="search-input" />
        </div>
      </Link>

      {/* Giữa: các icon menu */}
      <ul className="nav-list">
        <li>
          <Link to="/">
            <img src={homeIcon} alt="Home" className="nav-icon" />
          </Link>
        </li>
        <li>
          <Link to="/friends">
            <img src={friendsIcon} alt="Friends" className="nav-icon" />
          </Link>
        </li>
        <li>
          <Link to="/video">
            <img src={videoIcon} alt="Video" className="nav-icon" />
          </Link>
        </li>
        <li>
          <Link to="/market">
            <img src={marketIcon} alt="Market" className="nav-icon" />
          </Link>
        </li>
        <li>
          <Link to="/game">
            <img src={gameIcon} alt="Game" className="nav-icon" />
          </Link>
        </li>
      </ul>

      {/* Bên phải: menu + messenger + bell + avatar */}
      <div className="nav-right">
        <img src={menuIcon} alt="Menu" className="right-icon" />
        <img src={messengerIcon} alt="Messenger" className="right-icon" />
        <img src={bellIcon} alt="Notifications" className="right-icon" />
        <img src="/user-avatar-1.png" alt="Avatar" className="avatar" />
      </div>
    </nav>
  );
};
 
export default NavBar;