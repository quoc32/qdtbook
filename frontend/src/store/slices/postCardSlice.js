// postCardSlice.js
import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";

// Action async gọi API
export const fetchPosts = createAsyncThunk(
  "postCards/fetchPosts",
  async () => {
    const res = await fetch(import.meta.env.VITE_API_URL + "/posts", {
      credentials: "include", // nếu cần cookie JSESSIONID
    });
    return await res.json();
  }
);

const postCardSlice = createSlice({
  name: "postCards",
  initialState: {
    posts: [],
    loading: false,
    error: null,
  },
  reducers: {
    clear: (state) => {
      state.posts = [];
      state.loading = false;
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchPosts.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchPosts.fulfilled, (state, action) => {
        state.loading = false;
        state.posts = action.payload;
      })
      .addCase(fetchPosts.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message;
      });
  },
});

export const { clear } = postCardSlice.actions;
export default postCardSlice.reducer;
