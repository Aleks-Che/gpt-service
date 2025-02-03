import { createSlice } from '@reduxjs/toolkit';

import type { Conversation } from '../../types';

interface ConversationsState {
  conversations: Conversation[];
}

const initialState: ConversationsState = {
  conversations: [],
};

export const conversationsSlice = createSlice({
  name: 'conversations',
  initialState,
  reducers: {
    setConversations: (state, action) => {
      state.conversations = action.payload;
    },
    addConversation: (state, action) => {
      state.conversations.push(action.payload);
    },
  },
});

export const { setConversations, addConversation } = conversationsSlice.actions;
export default conversationsSlice.reducer;
