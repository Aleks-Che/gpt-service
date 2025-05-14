import React from 'react';
import styled from 'styled-components';

const ProfileContainer = styled.div`
  padding: 2rem;
`;

const Profile: React.FC = () => {
    return (
        <ProfileContainer>
            <h1>Профиль пользователя</h1>
            <p>Страница в разработке</p>
        </ProfileContainer>
    );
};

export default Profile;
