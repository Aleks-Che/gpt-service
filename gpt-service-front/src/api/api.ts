import axios from 'axios';
import { EventSourcePolyfill } from 'event-source-polyfill';

import { LlmModel } from '../types';

const API_URL = process.env.REACT_APP_API_URL;

const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('auth_token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const fetchModels = async (): Promise<LlmModel[]> => {
  const response = api.get('/api/models');
  return (await response).data;
};

export const fetchChats = () => api.get('/api/chat');

export const createChat = (message: string, modelId: number) => 
  api.post('/api/chat/create', { message, modelId });


export const generateResponse = (chatId: string, messageId?: string) => {
  const token = localStorage.getItem('auth_token');
  console.log('Using token:', token);
  
  const url = messageId 
    ? `${API_URL}/api/chat/${chatId}/generate?messageId=${messageId}`
    : `${API_URL}/api/chat/${chatId}/generate`;
    
  const eventSource = new EventSourcePolyfill(url, {
    headers: {
      'Authorization': `Bearer ${token}`,
      'Accept': 'text/event-stream'
    }
  });
  
  return eventSource;
};



export const getCurrentUser = () => api.get('/api/users/current');

export const registerUser = (userData: {
  username: string;
  firstName: string;
  lastName: string;
  city: string;
  country: string;
  password: string;
  email: string;
  phoneNumber: string;
}) => api.post('/api/auth/register', userData);

export const login = (credentials: { username: string; password: string }) => 
  api.post('/api/auth/login', credentials);

export const checkAuth = () => api.get('/api/auth/check');