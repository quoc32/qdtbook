
import React, { useEffect, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import style from "./HomeMainContent.module.css"
import CreatePost from './CreatePost';
import PostCard from './PostCard';
import { fetchPosts } from '../store/slices/postCardSlice';

const HomeMainContent = () => {
  const dispatch = useDispatch();
  const { posts, loading, error } = useSelector((state) => state.postCards);
  // Gọi API khi component được mount
  useEffect(() => {
    dispatch(fetchPosts());
  }, [dispatch]);
  
  console.log(posts);
  console.log(loading);
  console.log(error);

  // >> Render
  return (
    <div>
      {/* Nội dung chính */}
      <main className={style["main-content"]}>
        <CreatePost />
        { loading && <p>Loading posts...</p> }
        { error && <p>Error loading posts: {error}</p> }
        {/* // >> Render Các bài Post */}
        { posts && posts.map((post) => (
          <PostCard
            key={post.id}
            avatar={post.author.avatarURL || null}
            name={post.author.fullName || "Unknown User"}
            time={new Date(post.createdAt).toLocaleString() || "Unknown Time"}
            content={post.content || ""}
            image={post.media && post.media.length > 0 ? post.media[0].mediaUrl : null}
            likes={post.likes || 0}
            comments={post.comments || 0}
            shares={post.shares || 0}
          />
        )) }
        {/* Demo bài Post */}
        {Array.from({ length: 2 }).map((_, i) => (
          <PostCard
            key={i + 1000}
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