import React, { useState } from 'react';
import styled from 'styled-components';

import { login } from '../../api/api';
import { useAuth } from '../../hooks/useAuth';

const Form = styled.form`
  width: 80%;
  display: flex;
  flex-direction: column;
  gap: 1rem;
`;

const Input = styled.input`
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
`;

const Button = styled.button`
  padding: 0.75rem;
  background: #10a37f;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
`;

const RegisterButton = styled.button`
  padding: 0.75rem;
  background: transparent;
  border: 1px solid #10a37f;
  color: #10a37f;
  border-radius: 4px;
  cursor: pointer;
`;

interface LoginFormProps {
  onRegister: () => void;
}

const LoginForm: React.FC<LoginFormProps> = ({ onRegister }) => {
  const { login: authLogin } = useAuth();
  const [formData, setFormData] = useState({
    username: '',
    password: ''
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await login(formData);
      const { token, user } = response.data;

      localStorage.setItem('auth_token', token);
      authLogin({ user, token });
    } catch (error) {
      console.error('Login failed:', error);
    }
  };

  return (
    <Form onSubmit={handleSubmit}>
      <Input
        placeholder="Email или имя пользователя"
        name="username"
        value={formData.username}
        onChange={handleChange}
      />
      <Input
        type="password"
        placeholder="Пароль"
        name="password"
        value={formData.password}
        onChange={handleChange}
      />
      <Button type="submit">Войти</Button>
      <RegisterButton onClick={onRegister}>Регистрация</RegisterButton>
    </Form>
  );
};

export default LoginForm;
