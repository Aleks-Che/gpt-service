import { LogOut } from 'lucide-react'; // Add this import
import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate, useLocation } from 'react-router-dom';
import styled from 'styled-components';

import { useQuery } from '@tanstack/react-query';

import { setChats } from '../store/slices/chatsSlice';
import { fetchChats } from '../api/api';
import { RootState } from '../store';
import { Chat } from '../types';
import AnimatedLogo from './AnimatedLogo';

import "@fontsource/montserrat/200.css";
import "@fontsource/montserrat/400.css";

const SidebarContainer = styled.div`
  width: 260px;
  background-color: #202123;
  color: white;
  padding: 1rem;
  display: flex;
  flex-direction: column;
  font-family: roboto;
`;

const NewChatButton = styled.button<{ $isActive?: boolean }>`
  width: 100%;
  padding: 0.75rem;
  background-color: ${props => props.$isActive ? '#343541' : 'transparent'};
  border: ${props => props.$isActive ? '1px solid #565869' : 'none'};
  color: white;
  border-radius: 4px;
  cursor: pointer;
  margin-bottom: 1rem;
  font-family: montserrat;
  font-weight: 300;
  
  &:hover {
    background-color: #343541;
  }
`;

const ChatList = styled.div`
  flex: 1;
  overflow-y: auto;
  margin: 1rem 0;
  display: flex;
  flex-direction: column;
  gap: 4px;
`;

const Logo = styled.div`
  font-size: 1.5rem;
  font-weight: bold;
  margin-bottom: 2rem;
  cursor: pointer;
  text-align: center;
  display: flex;
  justify-content: flex-start;
  align-items: center;
  gap: 12px;
  padding-left: 8px;
  font-family: montserrat;
  font-weight: 200;
`;


const ButtonsContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 8px;
`;

const ProfileSection = styled.div`
  display: flex;
  align-items: center;
  gap: 8px;
`;

const ProfileButton = styled.button<{ $isActive?: boolean }>`
  width: 100%;
  padding: 0.75rem;
  background-color: ${props => props.$isActive ? '#343541' : 'transparent'};
  border: ${props => props.$isActive ? '1px solid #565869' : 'none'};
  color: white;
  cursor: pointer;
  border-radius: 4px;
  font-family: montserrat;
  font-weight: 300;
  
  &:hover {
    background-color: #343541;
  }
`;

const LogoutButton = styled.button`
  background: none;
  border: none;
  cursor: pointer;
  padding: 8px;
  display: flex;
  align-items: center;
  color: #666;
  
  &:hover {
    color:rgb(8, 107, 63);
  }
`;

const ChatButton = styled(ProfileButton) <{ $isActive?: boolean }>`
  text-align: left;
  padding: 12px;
  margin: 4px 0;
  font-size: 14px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  transition: background-color 0.2s;
  
  &:hover {
    background-color: #343541;
  }
`;

const Sidebar: React.FC = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const location = useLocation();
  const currentChatId = location.pathname.split('/').pop();
  const user = useSelector((state: RootState) => state.auth.user);
  const isAdmin = user?.roles.includes('ADMIN');

  const { data: chats } = useQuery<Chat[], Error>({
    queryKey: ['chats'],
    queryFn: async () => {
      const response = await fetchChats();
      dispatch(setChats(response.data));
      return response.data;
    }
  });

  const handleChatSelect = (chatId: number) => {
    navigate(`/chat/${chatId}`);
  };

  const handleLogout = () => {
    localStorage.removeItem('auth_token');
    window.location.href = '/login';
  };

  return (
    <SidebarContainer>
      <Logo onClick={() => navigate('/')}>
        <AnimatedLogo />
        GPT SERVICE
      </Logo>
      <NewChatButton
        onClick={() => navigate('/chat')}
        $isActive={location.pathname === '/'}
      >
        Новый чат
      </NewChatButton>
      <ChatList>
        {chats?.map((chat) => (
          <ChatButton
            key={chat.id}
            onClick={() => handleChatSelect(chat.id)}
            $isActive={String(chat.id) === currentChatId}
          >
            {chat.title}
          </ChatButton>
        ))}
      </ChatList>
      <ButtonsContainer>
        {isAdmin && (
          <ProfileButton
            onClick={() => navigate('/admin')}
            $isActive={location.pathname === '/admin'}
          >
            Админка
          </ProfileButton>
        )}
        <ProfileSection>
          <ProfileButton
            onClick={() => navigate('/profile')}
            $isActive={location.pathname === '/profile'}
          >
            Профиль
          </ProfileButton>
          <LogoutButton onClick={handleLogout}>
            <LogOut size={20} />
          </LogoutButton>
        </ProfileSection>
      </ButtonsContainer>
    </SidebarContainer>
  );

};

export default Sidebar;