import React from 'react';
import styled from 'styled-components';

const AdminContainer = styled.div`
  padding: 2rem;
`;

const Admin: React.FC = () => {
    return (
        <AdminContainer>
            <h1>Админ панель</h1>
            {/* Здесь будет контент админки */}
        </AdminContainer>
    );
};

export default Admin;
