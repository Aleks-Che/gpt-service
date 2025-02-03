import axios from 'axios';

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

export const fetchModels = () => api.get('/api/models');

export const fetchConversations = () => api.get('/api/conversations');

export const createConversation = (message: string, modelId: number) => 
  api.post('/api/conversations', { message, modelId });

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