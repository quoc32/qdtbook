
import React from 'react';
import style from "./HomeMainContent.module.css"
import CreatePost from './CreatePost';
import PostCard from './PostCard';

const HomeMainContent = () => {
  return (
    <div>
      {/* Nội dung chính */}
      <main className={style["main-content"]}>
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
    </div>
  );
}
 
export default HomeMainContent;