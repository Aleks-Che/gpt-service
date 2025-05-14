import React, { useState } from 'react';
import styled from 'styled-components';

import { registerUser } from '../../api/api';

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

const BackButton = styled.button`
  padding: 0.75rem;
  background: transparent;
  border: 1px solid #10a37f;
  color: #10a37f;
  border-radius: 4px;
  cursor: pointer;
`;

interface RegisterFormProps {
  onBack: () => void;
}

const ErrorMessage = styled.span`
  color: #ff4444;
  font-size: 0.8rem;
  margin-top: -0.5rem;
`;

interface FormData {
  username: string;
  firstName: string;
  lastName: string;
  city: string;
  country: string;
  password: string;
  email: string;
  phoneNumber: string;
}

interface FormErrors {
  username?: string;
  email?: string;
  phoneNumber?: string;
}

const AnimatedForm = styled(Form)`
  opacity: 0;
  animation: fadeIn 0.3s ease-in-out 0.2s forwards;

  @keyframes fadeIn {
    from {
      opacity: 0;
    }
    to {
      opacity: 1;
    }
  }
`;

const RegisterForm: React.FC<RegisterFormProps> = ({ onBack }) => {
  const [isSuccess, setIsSuccess] = useState(false);
  const [formData, setFormData] = useState<FormData>({
    username: '',
    firstName: '',
    lastName: '',
    city: '',
    country: '',
    password: '',
    email: '',
    phoneNumber: ''
  });

  const [errors, setErrors] = useState<FormErrors>({});

  const validateField = (name: string, value: string): string => {
    switch (name) {
      case 'username':
        if (!/^[a-zA-Z0-9_-]+$/.test(value)) {
          return 'Имя пользователя может содержать только буквы, цифры, дефис и подчеркивание';
        }
        break;
      case 'email':
        if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value)) {
          return 'Введите корректный email адрес';
        }
        break;
      case 'phoneNumber':
        if (!/^[\d()+\-\s]+$/.test(value)) {
          return 'Телефон может содержать только цифры и символы +()-';
        }
        break;
    }
    return '';
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));

    const error = validateField(name, value);
    setErrors(prev => ({
      ...prev,
      [name]: error
    }));
  };

  const SuccessMessage = styled.div`
  text-align: center;
  color: #10a37f;
  font-size: 1.2rem;
  margin: 2rem 0;
`;

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const newErrors: FormErrors = {};
    let hasErrors = false;

    Object.entries(formData).forEach(([key, value]) => {
      const error = validateField(key, value);
      if (error) {
        newErrors[key as keyof FormErrors] = error;
        hasErrors = true;
      }
    });

    if (hasErrors) {
      setErrors(newErrors);
      return;
    }

    try {
      await registerUser(formData);
      setIsSuccess(true);
    } catch (error) {
      console.error('Registration failed:', error);
    }
  };

  if (isSuccess) {
    return (
      <AnimatedForm>
        <SuccessMessage>
          Пользователь успешно зарегистрирован!
          <br />
          Теперь вы можете войти.
        </SuccessMessage>
        <BackButton onClick={onBack}>Назад</BackButton>
      </AnimatedForm>
    );
  } else return (
    <AnimatedForm onSubmit={handleSubmit}>
      <Input
        placeholder="Имя пользователя"
        name="username"
        value={formData.username}
        onChange={handleChange}
      />
      {errors.username && <ErrorMessage>{errors.username}</ErrorMessage>}

      <Input
        placeholder="Имя"
        name="firstName"
        value={formData.firstName}
        onChange={handleChange}
      />

      <Input
        placeholder="Фамилия"
        name="lastName"
        value={formData.lastName}
        onChange={handleChange}
      />

      <Input
        placeholder="Город"
        name="city"
        value={formData.city}
        onChange={handleChange}
      />

      <Input
        placeholder="Страна"
        name="country"
        value={formData.country}
        onChange={handleChange}
      />

      <Input
        type="password"
        placeholder="Пароль"
        name="password"
        value={formData.password}
        onChange={handleChange}
      />

      <Input
        type="email"
        placeholder="Email"
        name="email"
        value={formData.email}
        onChange={handleChange}
      />
      {errors.email && <ErrorMessage>{errors.email}</ErrorMessage>}

      <Input
        placeholder="Телефон"
        name="phoneNumber"
        value={formData.phoneNumber}
        onChange={handleChange}
      />
      {errors.phoneNumber && <ErrorMessage>{errors.phoneNumber}</ErrorMessage>}

      <Button type="submit">Зарегистрироваться</Button>
      <BackButton onClick={onBack}>Назад</BackButton>
    </AnimatedForm>
  );
};

export default RegisterForm;