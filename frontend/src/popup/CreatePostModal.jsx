import { useState, useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";

import { changeVisibility, changeContent as changePostStateContent } from "../store/slices/postSlice";
import { changeState as changePopupAnnouncementState } from "../store/slices/popupAnnouncementSlice";
import styles from "./CreatePostModal.module.css";
import { getVideoThumbnail } from "../utils/utils";
import SimpleButton1 from "../components/SimpleButton1";
import Announcement from "../components/Announcement";

// >> Options trong modal Chọn đối tượng bài viết
const ArticleSubjectSelectorOptions = [
  { id: "public", label: "Công khai", desc: "Bất kì ai " },
  { id: "friends", label: "Bạn bè", desc: "Chỉ hiển thị với một số bạn bè" },
  { id: "only_me", label: "Chỉ mình tôi", desc: "Chỉ bạn mới thấy được" },
];

// >> Item trong modal Chọn đối tượng bài viết
const ArticleSubjectSelectorItems = ({ id, label, desc }) => {
  const dispatch = useDispatch();
  const visibility = useSelector((state) => state.post.visibility);
  return (
    <div
      className={`${styles["ArticleSubjectSelectorItems-container"]} d-flex align-items-center justify-content-between px-3 py-2 border-bottom ${
        visibility === id ? "bg-light" : ""
      }`}
      style={{ cursor: "pointer" }}
      onClick={() => dispatch( changeVisibility({ visibility: id }) )}
    >
      <div>
        <div className="fw-semibold">{label}</div>
        <div className="text-muted small">{desc}</div>
      </div>
      <input
        type="radio"
        name="audience"
        checked={visibility === id}
        onChange={() => dispatch(changeVisibility({ visibility: id }))}
      />
    </div>
  );
};

// >> Modal Chọn đối tượng bài viết
const ArticleSubjectSelector = ({ onClose }) => {
  const visibilityRange = useSelector((state) => state.post.visibilityOptions);

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

// >> Modal Tạo bài viết
export default function CreatePostModal({ isOpen, onClose }) {
  const [loading, setLoading] = useState(false);
  const [files, setFiles] = useState([]);
  const [images, setImages] = useState([]);
  const [openArticleSubjectSelector, setOpenArticleSubjectSelector] = useState(false);

  // >> Slice auth state
  const auth = useSelector((state) => state.auth);
  const authId = auth.id;

  // >> Slice post state
  const dispatch = useDispatch();
  const post = useSelector((state) => state.post);
  const content = post.content;
  const visibility = post.visibility;
  const labels = post.labels;
  const labelIcons = post.labelIcons;

  // >> Slice popupAnnouncement state
  const announcementContent = useSelector((state) => state.content);

  // >> Xử lý tự động ẩn hiện thông báo

  // >> Xử lý hiển thị ảnh preview 
  useEffect(() => {
    const generatePreviews = async () => {
      const previews = await Promise.all(
        files.map(async (file) => {
          if (file.type.startsWith("image")) {
            return {
              name: file.name,
              type: "image",
              url: URL.createObjectURL(file),
            };
          } else if (file.type.startsWith("video")) {
            const thumb = await getVideoThumbnail(file);
            return {
              name: file.name,
              type: "video",
              url: thumb, // chỉ lưu thumbnail
            };
          }
          return null;
        })
      );
      setImages(previews.filter(Boolean));
    };

    generatePreviews();
  }, [files]);

  // >> Xóa ảnh preview
  const handleClearMedia = () => {
    setFiles([]);
    setImages([]);
  };

  // >> Hàm xử lý khi chọn file
  const handleFileChange = (e) => {
    const newFiles = Array.from(e.target.files); // chuyển FileList thành mảng
    setFiles((prevFiles) => [...prevFiles, ...newFiles]);
  };
  // >> Xử lý khi nhấn Đăng
  const handleSubmit = async () => {
    if ((!content && files.length === 0) || loading) return;

    setLoading(true);
    try {
      let mediaArray = [];

      // 1. Nếu có file thì upload
      if (files.length > 0) {
        const formData = new FormData();
        files.forEach(file => formData.append("files", file));

        const uploadRes = await fetch(import.meta.env.VITE_API_URL + "/media/upload", {
          method: "POST",
          body: formData,
          credentials: "include"
        });
        if (!uploadRes.ok) throw new Error("Upload thất bại");

        const uploadedFiles = await uploadRes.json();
        const uploadedURLs = uploadedFiles.urls; // giả sử backend trả { urls: [...] }

        mediaArray = uploadedURLs.map(fileName => ({
          media_type: fileName.match(/\.(mp4|mkv|avi)$/i) ? "video" : "image",
          // media_url: `${import.meta.env.VITE_API_URL}/media/files/${fileName}`
          media_url: `${import.meta.env.VITE_API_URL}${fileName}`
        }));
      }

      // 2. Tạo post (dù mediaArray rỗng cũng ok)
      const postRes = await fetch(import.meta.env.VITE_API_URL + "/posts", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify({
          author_id: authId, // bạn thay bằng userId thật
          content,
          visibility,
          post_type: "normal",
          media: mediaArray
        })
      });
      if (!postRes.ok) throw new Error("Tạo post thất bại");

      const newPost = await postRes.json();
      console.log("Tạo bài viết thành công:", newPost);

      // Hiển thị thông báo thành công
      dispatch(changePopupAnnouncementState({ content: "Đăng bài viết thành công!", successfull: true }));
      // Reset
      setFiles([]);
      setImages([]);
      onClose();
      //
      dispatch( changePostStateContent({ content: "" }) );

    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };


  // >> Render
  if (!isOpen) return null;
  return (
    <div className={styles["modal-overlay"]}>

      {openArticleSubjectSelector || 
      (<div className={styles["modal"]}>

        {/* Header */}
        <div className={styles["modal-header"]}>
          <div><h2>Tạo bài viết</h2></div>
          <button
            className={styles["close-btn"]}
            onClick={() => { setOpenArticleSubjectSelector(false); onClose(); }}
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
              <img src={labelIcons[visibility]} alt="Bạn bề" width="10" height="10" style={{marginBottom: "4px", marginRight: "5px"}}/>
              {labels[visibility] || ""}
            </button>
          </div>
        </div>

        {/* Input */}
        <textarea
          className={styles["post-input"]}
          placeholder="Tên ơi, bạn đang nghĩ gì thế?"
          value={content}
          onChange={ e => dispatch( changePostStateContent({content : e.target.value}) ) }
        ></textarea>

        {/* File input */}
        <div>
          <input
            type="file"
            id="fileInput"
            multiple
            accept="image/*,video/*,video/x-matroska"
            onChange={handleFileChange}
            className={styles["file-input"]}
            style={{ display: "none" }}
          />
          <div className={styles["image-preview-container"]}>
            <span 
              onClick={handleClearMedia}
              style={{display: images.length > 0 ? "block" : "none"}}
            >
              <SimpleButton1 imageSrc={"x.png"} imageAlt={"x"} noTooltip></SimpleButton1>
            </span>

            {images.map((media) => (
              <div key={media.name} className={styles["image-preview-item"]}>
                <img
                  src={media.url}
                  alt={media.name}
                  className={styles["image-preview"]}
                />
                {media.type === "video" && (
                  <div className={styles["video-overlay"]}>
                    <img src="icons-bundle-3-play.png" alt="video icon" width="24" height="24"/>
                  </div>
                )}
              </div>
            ))}

          </div>
        </div>

        {/* Color and emoji selector */}
        <div></div>

        {/* Add to post */}
        <div className={styles["add-section"]}>
          <span>Thêm vào bài viết của bạn</span>
          <div>
            <label htmlFor="fileInput"><span><SimpleButton1 imageSrc={"media-icon-1.png"} imageAlt={"Ảnh/Video"} tooltipText={"Ảnh/Video"}></SimpleButton1></span></label>
            <SimpleButton1 imageSrc={"gan-the-nguoi-khac.png"} imageAlt={"Gắn thẻ người khác"} tooltipText={"Gắn thẻ người khác"}></SimpleButton1>
            <SimpleButton1 imageSrc={"cam-xuc-hoat-dong.png"} imageAlt={"Cảm xúc/Hoạt động"} tooltipText={"Cảm xúc/Hoạt động"}></SimpleButton1>
            <SimpleButton1 imageSrc={"check-in.png"} imageAlt={"Check in"} tooltipText={"Check in"}></SimpleButton1>
            <span><SimpleButton1 imageSrc={"three-dot-1.png"} imageAlt={"Xem thêm"} tooltipText={"Xem thêm"}></SimpleButton1></span>
          </div>
        </div>

        {/* Post button */}
        <button
          className={styles["post-btn"]}
          onClick={handleSubmit}
          disabled={(!content && files.length == 0) || loading}
        >
          {loading ? "Đang đăng..." : "Đăng"}
        </button>

      </div>)}

      {/* // todo: Visibility Modal */}
      {openArticleSubjectSelector && (
        <ArticleSubjectSelector 
          onClose={() => setOpenArticleSubjectSelector(false)}
        />
      )}

    </div>
  );
}
