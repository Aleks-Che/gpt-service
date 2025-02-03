export interface Conversation {
  id: number;
  title: string;
  createdAt: string;
}

export interface LlmModel {
  id: number;
  name: string;
  description: string;
}

export interface User {
  id: number;
  username: string;
  email: string;
}
