import React, { useState } from 'react';
import styled from 'styled-components';

import LoginForm from './LoginForm';
import P5Background from './P5Background';
import RegisterForm from './RegisterForm';

const AuthWrapper = styled.div`
  width: 100vw;
  height: 100vh;
  position: relative;
`;

const P5Container = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  z-index: 0;
`;

const AuthContainer = styled.div<{ $isRegistering: boolean }>`
  position: relative;
  z-index: 1;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 500px;
  height: ${props => props.$isRegistering ? '665px' : '325px'};
  background: rgba(255, 255, 255, 0.95);
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0,0,0,0.1);
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 2rem;
  transition: height 0.3s ease-in-out;
`;

const GoogleButton = styled.button`
  width: 80%;
  padding: 1rem;
  margin: 1rem 0;
  background: #4285f4;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
`;

const Auth: React.FC = () => {
    const [isRegistering, setIsRegistering] = useState(false);

    return (
        <AuthWrapper>
            <P5Container>
                <P5Background width={window.innerWidth} height={window.innerHeight} />
            </P5Container>
            <AuthContainer $isRegistering={isRegistering}>
                {/* existing content */}
                <GoogleButton>Войти через Google</GoogleButton>
                <div>или</div>
                {isRegistering ? (
                    <RegisterForm onBack={() => setIsRegistering(false)} />
                ) : (
                    <LoginForm onRegister={() => setIsRegistering(true)} />
                )}
            </AuthContainer>
        </AuthWrapper>
    );
};

export default Auth;
