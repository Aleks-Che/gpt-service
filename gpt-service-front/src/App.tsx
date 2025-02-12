import React, { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';
import { useDispatch } from 'react-redux';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import styled from 'styled-components';

import Auth from './components/Auth/Auth';
import { setAuth } from './store/slices/authSlice';
import { checkAuth } from './api/api';
import Admin from './components/Admin';
import Chat from './components/Chat';
import LoadingSpinner from './components/LoadingSpinner';
import Profile from './components/Profile';
import Sidebar from './components/Sidebar';
import { RootState } from './store';

const AppContainer = styled.div`
  display: flex;
  height: 100vh;
`;

const MainContent = styled.div`
  flex: 1;
  overflow: hidden;
`;

const ProtectedAdminRoute: React.FC<{ element: React.ReactElement }> = ({ element }) => {
  const user = useSelector((state: RootState) => state.auth.user);
  const isAdmin = user?.roles.includes('ADMIN');

  return isAdmin ? element : <Navigate to="/" />;
};

const App: React.FC = () => {
  const dispatch = useDispatch();
  const [isLoading, setIsLoading] = useState(true);
  const isAuthenticated = useSelector((state: RootState) => state.auth.isAuthenticated);

  useEffect(() => {
    const checkAuthToken = async () => {
      const token = localStorage.getItem('auth_token');
      if (token) {
        try {
          const response = await checkAuth();
          dispatch(setAuth({ user: response.data, token }));
        } catch (error) {
          localStorage.removeItem('auth_token');
        }
      }
      setIsLoading(false);
    };

    checkAuthToken();
  }, [dispatch]);

  if (isLoading) {
    return <LoadingSpinner />;
  }

  if (!isAuthenticated) {
    return <Auth />;
  }

  return (
    <Router>
      <AppContainer>
        <Sidebar />
        <MainContent>
          <Routes>
            <Route path="/" element={<Chat />} />
            <Route path="/chat/:chatId" element={<Chat />} />
            <Route path="/profile" element={<Profile />} />
            <Route
              path="/admin"
              element={<ProtectedAdminRoute element={<Admin />} />}
            />
            <Route path="*" element={<Navigate to="/" />} />
          </Routes>
        </MainContent>
      </AppContainer>
    </Router>
  );
};

export default App;
