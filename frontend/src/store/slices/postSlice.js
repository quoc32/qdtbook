import { createSlice } from "@reduxjs/toolkit";

const postSlice = createSlice({
  name: "post",
  initialState: { 
    content: "",
    visibility : "public",
    post_type : "normal",
    media : [],

    // Static attributes
    visibilityOptions : ["public", "friends", "only_me"],
    labels : {
      friends: "Bạn bè ▾",
      public: "Công khai ▾",
      only_me: "Chỉ mình tôi ▾"
    },
    labelIcons : {
      friends: "icons-bundle-8-ban-be.png",
      public: "cong-khai.png",
      only_me: "chi-minh-toi.png"
    },
  },
  reducers: {
    loadMedia: (state, action) => {
      const { media } = action.payload;
      state.media.push(media);
    },
    changeVisibility: (state, action) => {
      const { visibility } = action.payload;
      state.visibility = visibility;
    },
    changeContent: (state, action) => {
      const { content } = action.payload;
      state.content = content;
    },
  },
});

export const { loadMedia, changeVisibility, changeContent } = postSlice.actions;
export default postSlice.reducer;
