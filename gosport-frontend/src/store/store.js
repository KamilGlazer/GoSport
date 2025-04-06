import { configureStore } from "@reduxjs/toolkit";
import authReducer from "./authSlice";
import { persistReducer,persistStore } from "redux-persist";
import storage from 'redux-persist/lib/storage';

const persistConfig = {
    key: 'auth', 
    storage, 
    whitelist: ['user', 'token'] 
};

const persistedReducer = persistReducer(persistConfig, authReducer);

export const store = configureStore({
    reducer: {
      auth: persistedReducer 
    },
    middleware: (getDefaultMiddleware) =>
      getDefaultMiddleware({
        serializableCheck: {
          ignoredActions: ['persist/PERSIST'],
        },
      }),
  });

export const persistor = persistStore(store); 
