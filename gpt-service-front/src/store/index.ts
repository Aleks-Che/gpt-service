import { configureStore } from '@reduxjs/toolkit';

import authReducer from './slices/authSlice';
import chatsReducer from './slices/chatsSlice';
import modelsReducer from './slices/modelsSlice';
import dictionaryReducer from './dictionarySlice';

export const store = configureStore({
  reducer: {
    dictionary: dictionaryReducer,
    auth: authReducer,
    models: modelsReducer,
    chats: chatsReducer,
  },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
