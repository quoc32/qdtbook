
import './Home.css'
import PostCard from '../components/PostCard';
import TabSelector1 from '../components/TabSelector1';
import HomeMoreTabSelector1 from '../components/HomeMoreTabSelector1';
import CreatePost from '../components/CreatePost';

// Home.jsx
const Home = () => {
  return (
    <div className="home-layout">
      {/* Cột trái */}
      <aside className="sidebar left">
        {/* Tang cá nhân */}
        <TabSelector1
          imgSrc="./user-avatar-1.png"
          altText="User Avatar"
          contentText="Trang cá nhân"
        />
        {/* Meta AI */}
        <TabSelector1
          imgSrc="./meta-ai-icon.png"
          altText="Meta AI"
          contentText="Meta AI"
        />
        {/* Friends */}
        <TabSelector1
          imgSrc="./various-icon-1-friends.png"
          altText="Friends icon"
          contentText="Bạn bè"
        />
        {/* Groups */}
        <TabSelector1
          imgSrc="./various-icon-1-groups.png"
          altText="Groups icon"
          contentText="Nhóm"
        />
        {/* Groups */}
        <TabSelector1
          imgSrc="./various-icon-1-memories.png"
          altText="Memories icon"
          contentText="Kỷ niệm"
        />
        {/* Save */}
        <TabSelector1
          imgSrc="./various-icon-1-save.png"
          altText="Save icon"
          contentText="Đã lưu"
        />
        {/* Video */}
        <TabSelector1
          imgSrc="./various-icon-1-video2.png"
          altText="Video icon"
          contentText="Video"
        />
        {/* Xem thêm */}
        <HomeMoreTabSelector1 />

        <div className='black-line'></div>
        <span style={{ fontWeight: 600, color: "#65676b", fontSize: 12, marginTop: 0, marginBottom: 10 }}>
          Lối tắt của bạn
        </span>

        
      </aside>

      {/* Nội dung chính */}
      <main className="main-content">
        <CreatePost />
        {Array.from({ length: 20 }).map((_, i) => (
          <PostCard
            key={i}
            avatar={null}
            name="Tâm Chó Cò"
            time="1 giờ"
            content="Nếu Trên Người Có Quá Nhiều Chấy Đừng Lo Đã Có Chim Bò Đến Xử Lí"
            image={null}
            likes={770}
            comments={20}
            shares={1}
          />
        ))}
      </main>

      {/* Cột phải */}
      <aside className="sidebar right">
        {Array.from({ length: 40 }).map((_, i) => (
          <p key={i}>Người liên hệ {i + 1}</p>
        ))}
      </aside>
    </div>
  );
};

export default Home;

