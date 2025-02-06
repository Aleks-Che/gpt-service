import { createSlice } from '@reduxjs/toolkit';

import type { Chat } from '../../types';

interface ChatsState {
  chats: Chat[];
}

const initialState: ChatsState = {
  chats: [],
};

export const chatsSlice = createSlice({
  name: 'chats',
  initialState,
  reducers: {
    setChats: (state, action) => {
      state.chats = action.payload;
    },
    addChat: (state, action) => {
      state.chats.push(action.payload);
    },
  },
});

export const { setChats, addChat } = chatsSlice.actions;
export default chatsSlice.reducer;
