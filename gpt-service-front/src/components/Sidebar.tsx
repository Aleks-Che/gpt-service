import { LogOut } from 'lucide-react'; // Add this import
import React from 'react';
import { useSelector } from 'react-redux';
import { useNavigate, useLocation } from 'react-router-dom';
import styled from 'styled-components';

import { QueryFunction, useQuery } from '@tanstack/react-query';

import { fetchConversations } from '../api/api';
import { RootState } from '../store';
import { Conversation } from '../types';
import AnimatedLogo from './AnimatedLogo';

const SidebarContainer = styled.div`
  width: 260px;
  background-color: #202123;
  color: white;
  padding: 1rem;
  display: flex;
  flex-direction: column;
`;

const NewChatButton = styled.button`
  width: 100%;
  padding: 0.75rem;
  background-color: #343541;
  border: 1px solid #565869;
  color: white;
  border-radius: 4px;
  cursor: pointer;
  margin-bottom: 1rem;
`;

const ConversationList = styled.div`
  flex: 1;
  overflow-y: auto;
`;

const Logo = styled.div`
  font-size: 1.5rem;
  font-weight: bold;
  margin-bottom: 2rem;
  cursor: pointer;
  text-align: center;
  display: flex;
  justify-content: center;
  align-items: center;
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
    color: #2b7efb;
  }
`;

const Sidebar: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const user = useSelector((state: RootState) => state.auth.user);
  const isAdmin = user?.roles.includes('ADMIN');

  const { data } = useQuery<Conversation[], Error>({
    queryKey: ['conversations'],
    queryFn: async () => {
      const response = await fetchConversations();
      return response.data;
    }
  });

  const handleLogout = () => {
    localStorage.removeItem('auth_token');
    window.location.href = '/login';
  };

  const conversations = data || [];

  return (
    <SidebarContainer>
      <AnimatedLogo />
      <Logo onClick={() => navigate('/')}>GPT SERVICE</Logo>
      <NewChatButton>Новый чат</NewChatButton>
      <ConversationList>
        {conversations.map((conv: Conversation) => (
          <div key={conv.id}>{conv.title}</div>
        ))}
      </ConversationList>
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