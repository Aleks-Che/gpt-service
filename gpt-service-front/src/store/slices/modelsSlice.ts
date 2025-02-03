import { createSlice } from '@reduxjs/toolkit';

import type { LlmModel } from '../../types';

interface ModelsState {
  models: LlmModel[];
  selectedModel: LlmModel | null;
}

const initialState: ModelsState = {
  models: [],
  selectedModel: null,
};

export const modelsSlice = createSlice({
  name: 'models',
  initialState,
  reducers: {
    setModels: (state, action) => {
      state.models = action.payload;
    },
    setSelectedModel: (state, action) => {
      state.selectedModel = action.payload;
    },
  },
});

export const { setModels, setSelectedModel } = modelsSlice.actions;
export default modelsSlice.reducer;
