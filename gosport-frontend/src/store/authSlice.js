import { createSlice } from "@reduxjs/toolkit";


const initialState = {
    user: null,
    token : null,
    loading: false,
    error: null
};


const authSlice = createSlice({
    name: "auth",
    initialState,
    reducers : {
        loginStart: (state) => {
            state.loading = true;
            state.error = null;
        },
        loginSuccess: (state,action) => {
            state.loading = false;
            state.user = action.payload.user || {};
            state.token = action.payload.token;
            localStorage.setItem("token", action.payload.token);
        },
        loginFailure: (state,action) => {
            state.loading = false;
            state.error = action.payload;
        },
        logout : (state) => {
            state.user = null;
            state.token = null;
            localStorage.removeItem("token");
        },
        setAvatar: (state, action) => {
            if (state.user) {
                state.user.avatar = action.payload;
            }
        },
        updateUserProfile: (state,action) => {
            if (state.user) {
                state.user = { 
                  ...state.user, 
                  ...action.payload 
                };
              } else {
                state.user = action.payload;
              }
        },
        setUserProfile: (state, action) => {
            if (state.user) {
              state.user.profile = action.payload;
            } else {
              state.user = { profile: action.payload };
            }
          },
    }
});

export const {loginStart,loginSuccess,loginFailure,logout, setAvatar, updateUserProfile, setUserProfile  } = authSlice.actions;
export default authSlice.reducer;

