export interface Chat {
  id: number;
  title: string;
  createdAt: string;
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
