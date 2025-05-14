export interface Chat {
  id: number;
  title: string;
  user: User;
  model: LlmModel;
  messages: Message[];
  createdAt: string;
  updatedAt: string;
}

export interface Message {
  id: number;
  messageType: 'USER' | 'ASSISTANT';
  content: string;
  tokensCount: number;
  contentSummarize: string | null;
  summarizeTokensCount: number | null;
  createdAt: string;
  filePath: string | null;
  fileSizeBytes: number | null;
  fileMimeType: string | null;
}

export interface LlmModel {
  id: number;
  name: string;
  description: string;
  modelType: string;
  provider: string;
  apiEndpoint: string | null;
  apiToken: string | null;
  maxTokens: number;
  temperature: number;
  subscriptionIds: number[];
  isFavorite: boolean;
  isDefault: boolean;
  active: boolean;
}

export interface User {
  id: number;
  username: string;
  email: string;
}
