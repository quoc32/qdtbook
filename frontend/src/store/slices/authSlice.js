import { createSlice } from "@reduxjs/toolkit";

const authSlice = createSlice({
  name: "auth",
  initialState: { 
    id: 0, 
    email: "default@user.gmail.com", 
    gender: "custom",
    bio: "Default bio text",
    full_name: "full_name",
    first_name: "first_name",
    last_name: "last_name",
  },
  reducers: {
    loadAuth: (state, action) => {
      const { id, email, gender, bio, full_name, first_name, last_name } = action.payload;
      state.id = id;
      state.email = email;
      state.gender = gender;
      state.bio = bio;
      state.full_name = full_name;
      state.first_name = first_name;
      state.last_name = last_name;
    },
  },
});

export const { loadAuth } = authSlice.actions;
export default authSlice.reducer;
