import { configureStore } from "@reduxjs/toolkit";
import authReducer from "./slices/authSlice"
import postReducer from "./slices/postSlice"
import popupAnnouncementReducer from "./slices/popupAnnouncementSlice"
import postCardReducer from "./slices/postCardSlice"

const store = configureStore({
  reducer: {
    auth: authReducer,
    post: postReducer,
    popupAnnouncement: popupAnnouncementReducer,
    postCards: postCardReducer,
  },
});

export default store;
