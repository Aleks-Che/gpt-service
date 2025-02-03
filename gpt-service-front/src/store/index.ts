import { configureStore } from '@reduxjs/toolkit';

import authReducer from './slices/authSlice';
import dictionaryReducer from './dictionarySlice';

export const store = configureStore({
  reducer: {
    dictionary: dictionaryReducer,
    auth: authReducer,
  },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
