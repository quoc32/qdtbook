
import { useSelector, useDispatch } from "react-redux";
import { useEffect, useState } from "react";

import style from './Page.module.css'
import { loadAuth } from '../store/slices/authSlice'
import { changeState, clear as clearPopupAnnouncementState } from '../store/slices/popupAnnouncementSlice'
import { fetchPosts } from '../store/slices/postCardSlice';

import NavBar from '../components/NavBar';
import HomeContactList from '../components/HomeContactList';
import HomeLeftPanel from '../components/HomeLeftPanel';
import HomeMainContent from '../components/HomeMainContent';
import Announcement from "../components/Announcement";

// Home.jsx
const Home = () => {

  const dispatch = useDispatch();
  useEffect(() => {
    // Giả sử dữ liệu auth lấy từ localStorage hoặc API
    const storedAuth = JSON.parse(localStorage.getItem("auth"));

    dispatch(loadAuth(storedAuth));
  }, [dispatch]);

  // >> Slice popupAnnouncement state
  const announcementContent = useSelector((state) => state.popupAnnouncement.content);
  const announcementSuccess = useSelector((state) => state.popupAnnouncement.successfull);
  // >> Xử lý thông báo đăng bài Post thành công hay thất bại
  const [showAnnouncement, setShowAnnouncement] = useState(false);
  if (announcementContent && !showAnnouncement) {
    setShowAnnouncement(true);
    setTimeout(() => {
      setShowAnnouncement(false);
      dispatch(clearPopupAnnouncementState());

      console.log("Reloading page...");
      window.location.reload();

    }, 3000); // Hiển thị thông báo trong 3 giây
  }
  // // >> Xử lý load lại trang khi đăng bài Post thành công
  // useEffect(() => {
  //   dispatch(fetchPosts());
  // }, [dispatch]);

  // >> Render
  return (
    <div>
      <NavBar />

      <div className={`${style["home-layout"]} ${style["page-wapper"]}`}>
        {/* Cột trái */}
        <HomeLeftPanel />
  
        {/* Nội dung chính */}
        <HomeMainContent />
  
        {/* Cột phải */}
        <HomeContactList />

      </div>

      {showAnnouncement && <Announcement text={announcementContent} successfull={announcementSuccess}></Announcement>}

    </div>
  );
};

export default Home;

