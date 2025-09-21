import { createSlice } from "@reduxjs/toolkit";

const popupAnnouncementSlice = createSlice({
  name: "popupAnnouncement",
  initialState: { 
    content: null,
    successfull: true,
  },
  reducers: {
    changeState: (state, action) => {
      state.content = action.payload.content;
      state.successfull = action.payload.successfull;
    },
    clear: (state) => {
      state.content = null;
    }
  },
});

export const { changeState, clear } = popupAnnouncementSlice.actions;
export default popupAnnouncementSlice.reducer;
