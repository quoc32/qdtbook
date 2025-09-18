import { useState } from "react";
import styles from "./CreatePostModal.module.css";
import { createPost } from "../utils/CreatePost";

import SimpleButton1 from "../components/SimpleButton1";

const ArticleSubjectSelectorOptions = [
  { id: "public", label: "Công khai", desc: "Bất kì ai " },
  { id: "friends", label: "Bạn bè", desc: "Chỉ hiển thị với một số bạn bè" },
  { id: "only_me", label: "Chỉ mình tôi", desc: "Chỉ bạn mới thấy được" },
];

const ArticleSubjectSelectorItems = ({ id, label, desc, selected, onChange }) => {
  return (
    <div
      className={`${styles["ArticleSubjectSelectorItems-container"]} d-flex align-items-center justify-content-between px-3 py-2 border-bottom ${
        selected === id ? "bg-light" : ""
      }`}
      style={{ cursor: "pointer" }}
      onClick={() => onChange(id)}
    >
      <div>
        <div className="fw-semibold">{label}</div>
        <div className="text-muted small">{desc}</div>
      </div>
      <input
        type="radio"
        name="audience"
        checked={selected === id}
        onChange={() => onChange(id)}
      />
    </div>
  );
};

const ArticleSubjectSelector = ({ onClose, selected, setSelected }) => {

  return (
    <div className={`${styles["modal-ArticleSubjectSelector"]} animate__animated animate__fadeInRight animate__faster`}>
      <div className="p-1 pb-0">
        {/* Header */}
        <div className={styles["modal-header"]}>
          <button className={styles["close-btn"]} onClick={onClose} style={{position: "absolute"}}>
            <img src="icons-bundle-8-arrow-left-2.png" alt="arrow-left"
            style={{width: "12px", height: "12px", marginBottom: "2px"}}/>
          </button>
          <div><h2>Đối tượng của bài viết</h2></div>
        </div>

        {/* Ai có thể xem bài viết ... */}
        <span style={{fontWeight: 600, fontSize: "10px"}}>Ai có thể xem bài viết của bạn?</span>
        <p style={{fontSize: "10px", color: "#B0B3B8"}}>Bài viết của bạn sẽ hiển thị lên Bảng feet, trang cá nhân và trong kết quả tìm kiếm</p>
        
        {/* Options */}
        <div className="max-h-64 overflow-y-auto">
          {ArticleSubjectSelectorOptions.map((opt) => (
            <ArticleSubjectSelectorItems
              key={opt.id}
              id={opt.id}
              label={opt.label}
              desc={opt.desc}
              selected={selected}
              onChange={setSelected}
            />
          ))}
        </div>

        {/* Footer */}
        <div className={`${styles["ArticleSubjectSelector-footer-container"]} gap-2 border-t pt-2`}>
          <button onClick={onClose} >Tiếp</button>
          <button onClick={onClose} >Hủy</button>
        </div>
      </div>
    </div>
  );
}


export default function CreatePostModal({ isOpen, onClose }) {
  const [postContent, setPostContent] = useState("");
  const [loading, setLoading] = useState(false);
  const [openArticleSubjectSelector, setOpenArticleSubjectSelector] = useState(false);
  const [selectedArticleSubject, setSelectedArticleSubject] = useState("friends");

  const labels = {
    friends: "Bạn bè ▾",
    public: "Công khai ▾",
    only_me: "Chỉ mình tôi ▾"
  };

  const labelIcons = {
    friends: "icons-bundle-8-ban-be.png",
    public: "cong-khai.png",
    only_me: "chi-minh-toi.png"
  };

  if (!isOpen) return null;

  // >> Xử lý khi nhấn Đăng
  const handleSubmit = async () => {
    if (!postContent.trim()) return;
    setLoading(true);
    createPost(postContent)
      .then((data) => {
        alert("Đăng thành công: " + JSON.stringify(data));
        setLoading(false);
        setPostContent("");
        onClose();
      })
      .catch((err) => {
        alert("Có lỗi: " + err.message);
        setLoading(false);
      });
  };

  return (
    <div className={styles["modal-overlay"]}>

      {openArticleSubjectSelector || 
      (<div className={styles["modal"]}>

        {/* Header */}
        <div className={styles["modal-header"]}>
          <div><h2>Tạo bài viết</h2></div>
          <button
            className={styles["close-btn"]}
            onClick={() => { setPostContent(""); onClose(); }}
          >
            ✕
          </button>
        </div>

        {/* User info */}
        <div className={styles["modal-user"]}>
          <img src="/user-avatar-1.png" alt="Avatar" className={styles["user-avatar"]} />
          <div>
            <div className={styles["user-name"]}>Họ Tên</div>
            <button className={styles["privacy-btn"]} onClick={() => setOpenArticleSubjectSelector(true)}>
              <img src={labelIcons[selectedArticleSubject]} alt="Bạn bề" width="10" height="10" style={{marginBottom: "4px", marginRight: "5px"}}/>
              {labels[selectedArticleSubject] || ""}
            </button>
          </div>
        </div>

        {/* Input */}
        <textarea
          className={styles["post-input"]}
          placeholder="Tên ơi, bạn đang nghĩ gì thế?"
          value={postContent}
          onChange={(e) => setPostContent(e.target.value)}
        ></textarea>

        {/* Color and emoji selector */}
        <div></div>

        {/* Add to post */}
        <div className={styles["add-section"]}>
          <span>Thêm vào bài viết của bạn</span>
          <div>
            <SimpleButton1 imageSrc={"media-icon-1.png"} imageAlt={"Ảnh/Video"} tooltipText={"Ảnh/Video"}></SimpleButton1>
            <SimpleButton1 imageSrc={"gan-the-nguoi-khac.png"} imageAlt={"Gắn thẻ người khác"} tooltipText={"Gắn thẻ người khác"}></SimpleButton1>
            <SimpleButton1 imageSrc={"cam-xuc-hoat-dong.png"} imageAlt={"Cảm xúc/Hoạt động"} tooltipText={"Cảm xúc/Hoạt động"}></SimpleButton1>
            <SimpleButton1 imageSrc={"check-in.png"} imageAlt={"Check in"} tooltipText={"Check in"}></SimpleButton1>
            <SimpleButton1 imageSrc={"three-dot-1.png"} imageAlt={"Xem thêm"} tooltipText={"Xem thêm"}></SimpleButton1>
          </div>
        </div>

        {/* Post button */}
        <button
          className={styles["post-btn"]}
          onClick={handleSubmit}
          disabled={!postContent || loading}
        >
          {loading ? "Đang đăng..." : "Đăng"}
        </button>

      </div>)}

        {/* // todo: Audience Modal */}
        {openArticleSubjectSelector && (
          <ArticleSubjectSelector 
            onClose={() => setOpenArticleSubjectSelector(false)} 
            selected={selectedArticleSubject} 
            setSelected={setSelectedArticleSubject}
          />
        )}
    </div>
  );
}
