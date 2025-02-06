import axios from 'axios';

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
  api.post('/api/chat', { message, modelId });

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