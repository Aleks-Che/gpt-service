import React from 'react';
import ReactDOM from 'react-dom/client';
import { Provider } from 'react-redux';

import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

import App from './App';
import reportWebVitals from './reportWebVitals';
import { store } from './store';

import './index.css';

const queryClient = new QueryClient();

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);

root.render(
  <React.StrictMode>
    <Provider store={store} children={
      <QueryClientProvider client={queryClient}>
        <App />
      </QueryClientProvider>
    } />
  </React.StrictMode>
);
reportWebVitals();
