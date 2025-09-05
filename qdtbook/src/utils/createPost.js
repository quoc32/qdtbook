export async function createPost(content) {
  try {
    const res = await fetch("http://localhost:9999/servlet_test1/mongoTest", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ content }),
    });

    const text = await res.text(); // đọc nội dung trả về

    if (!res.ok) {
      throw new Error(`Server error ${res.status}: ${text}`);
    }

    // Nếu server trả JSON hợp lệ
    try {
      return JSON.parse(text);
    } catch {
      return { message: text }; // fallback nếu không phải JSON
    }
  } catch (err) {
    console.error("❌ createPost error:", err);
    throw err;
  }
}
