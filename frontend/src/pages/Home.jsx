
import { useSelector, useDispatch } from "react-redux";
import { useEffect } from "react";

import style from './Page.module.css'
import { loadAuth } from '../store/slices/authSlice'

import NavBar from '../components/NavBar';
import HomeContactList from '../components/HomeContactList';
import HomeLeftPanel from '../components/HomeLeftPanel';
import HomeMainContent from '../components/HomeMainContent';

// Home.jsx
const Home = () => {

  const dispatch = useDispatch();
  useEffect(() => {
    // Giả sử dữ liệu auth lấy từ sessionStorage hoặc API
    const storedAuth = JSON.parse(sessionStorage.getItem("auth")) || {
      id: 1,
      email: "user@example.com",
      gender: "male",
      bio: "Xin chào, tôi là user",
      full_name: "Nguyen Van A",
      first_name: "Nguyen",
      last_name: "A",
    };

    dispatch(loadAuth(storedAuth));
  }, [dispatch]);

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
    </div>
  );
};

export default Home;

