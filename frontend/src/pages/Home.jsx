
import style from './Page.module.css'

import NavBar from '../components/NavBar';
import HomeContactList from '../components/HomeContactList';
import HomeLeftPanel from '../components/HomeLeftPanel';
import HomeMainContent from '../components/HomeMainContent';

// Home.jsx
const Home = () => {
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

