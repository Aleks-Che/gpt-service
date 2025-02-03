import { Send } from 'lucide-react';
import React, { useState } from 'react';
import styled from 'styled-components';

import { useQuery } from '@tanstack/react-query';

import { fetchModels } from '../api/api';

const ChatContainer = styled.div`
  flex: 1;
  padding: 1rem;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  position: relative;
`;



const ModelSelect = styled.select`
  width: 200px;
  padding: 0.5rem;
  margin-bottom: 1rem;
  position: absolute;
  top: 1rem;
  left: 1rem;
`;

const InputContainer = styled.div`
  width: 800px;
  position: absolute;
  bottom: 6rem;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 8px;
`;

const MessageInput = styled.textarea`
  flex: 1;
  height: 100px;
  padding: 0.5rem;
  resize: none;
  border: 1px solid #e5e5e5;
  border-radius: 8px;
  outline: none;
  
  &:focus {
    border-color: rgb(82, 87, 96);
    box-shadow: 0 0 0 2px rgba(37, 49, 66, 0.1);
  }
`;

const SendButtonContainer = styled.div`
  display: flex;
  align-items: flex-end;
`;

const SendButton = styled.button`
  width: 25px;
  height: 25px;
  display: flex;
  align-items: center;
  justify-content: center;
  background:rgb(118, 118, 120);
  border: none;
  border-radius: 12px;
  cursor: pointer;
  color: white;
  
  &:hover {
    background:rgb(150, 150, 151);
  }
`;

const Chat: React.FC = () => {
    const { data: models } = useQuery({
        queryKey: ['models'],
        queryFn: fetchModels
    });

    const [selectedModel, setSelectedModel] = useState<string>('');
    const [message, setMessage] = useState<string>('');

    return (
        <ChatContainer>
            <ModelSelect
                value={selectedModel}
                onChange={(e) => setSelectedModel(e.target.value)}
                title={models?.data.find((m: any) => m.id === selectedModel)?.description}
            >
                {models?.data.map((model: any) => (
                    <option key={model.id} value={model.id}>
                        {model.name}
                    </option>
                ))}
            </ModelSelect>
            <InputContainer>
                <MessageInput
                    value={message}
                    onChange={(e) => setMessage(e.target.value)}
                    placeholder="Введите ваше сообщение..."
                />
                <SendButtonContainer>
                    <SendButton>
                        <Send size={25} />
                    </SendButton>
                </SendButtonContainer>
            </InputContainer>
        </ChatContainer>
    );
};

export default Chat;
