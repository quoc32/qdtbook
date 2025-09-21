
/**
 * Tạo ảnh thumbnail từ một tệp video trên trình duyệt.
 *
 * Hàm tạo phần tử <video> và <canvas>, tải dữ liệu khung hình đầu tiên (sự kiện 'loadeddata'),
 * vẽ khung hình đó lên canvas rồi xuất ảnh thumbnail dưới dạng Data URL (PNG, base64).
 * Sau khi sử dụng, URL tạm của video sẽ được giải phóng bằng URL.revokeObjectURL để tránh rò rỉ bộ nhớ.
 *
 * Lưu ý:
 * - Chỉ xử lý thành công khi video tải được dữ liệu khung hình đầu tiên; chưa có xử lý lỗi (onerror).
 * - Kích thước thumbnail bằng đúng kích thước khung hình gốc của video.
 * - Yêu cầu môi trường trình duyệt với hỗ trợ DOM và Canvas API.
 *
 * @param {File|Blob} file - Tệp video đầu vào (ví dụ: từ <input type="file">).
 * @returns {Promise<string>} Promise resolve với Data URL (PNG, base64) của thumbnail.
 *
 * @example
 * const file = fileInput.files[0];
 * const thumbnail = await getVideoThumbnail(file);
 * imageElement.src = thumbnail;
 */
const getVideoThumbnail = (file) => {
  return new Promise((resolve) => {
    const video = document.createElement("video");
    const canvas = document.createElement("canvas");

    video.preload = "metadata";
    video.src = URL.createObjectURL(file);
    video.muted = true; // tránh warning autoplay
    video.playsInline = true;

    // Khi đã load metadata
    video.onloadedmetadata = () => {
      // set thời điểm lấy thumbnail (0s)
      video.currentTime = 0;
    };

    // Khi seek tới frame đầu tiên thành công
    video.onseeked = () => {
      canvas.width = video.videoWidth;
      canvas.height = video.videoHeight;

      const ctx = canvas.getContext("2d");
      ctx.drawImage(video, 0, 0, canvas.width, canvas.height);

      const thumbnail = canvas.toDataURL("image/png");
      resolve(thumbnail);

      URL.revokeObjectURL(video.src);
    };
  });
};


export { getVideoThumbnail };