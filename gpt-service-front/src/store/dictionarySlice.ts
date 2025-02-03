import { createSlice, PayloadAction } from '@reduxjs/toolkit';

interface DictionaryState {
  models: any[];
  // Другие справочники
}

const initialState: DictionaryState = {
  models: [],
};

const dictionarySlice = createSlice({
  name: 'dictionary',
  initialState,
  reducers: {
    setModels: (state, action: PayloadAction<any[]>) => {
      state.models = action.payload;
    },
  },
});

export const { setModels } = dictionarySlice.actions;
export default dictionarySlice.reducer;
