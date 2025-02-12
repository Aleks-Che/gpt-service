import axios from 'axios';
import { Send, Edit, X, Copy, Trash2, Check, StopCircle, Repeat } from 'lucide-react';
import React, { useEffect, useRef, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useParams } from 'react-router-dom';
import styled from 'styled-components';

import { addChat } from '../store/slices/chatsSlice';
import { setModels, setSelectedModel } from '../store/slices/modelsSlice';
import { createChat, fetchChats, fetchModels, generateResponse } from '../api/api';
import { RootState } from '../store';
import { Chat as ChatType, Message as MessageType } from '../types';

const ChatContainer = styled.div`
  flex: 1;
  padding: 1rem;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  position: relative;
`;

const Message = styled.div<{ isUser: boolean }>`
  max-width: 70%;
  margin: ${props => props.isUser ? '2rem 1rem 2rem auto' : '2rem 1rem'};
  padding: 0.75rem;
  border-radius: 8px;
  background: ${props => props.isUser ? 'rgb(249 249 249 / var(--tw-bg-opacity, 1))' : '#F2F2F7'};
  color: ${props => props.isUser ? '#374151' : '#000000'};
  position: relative;
  font-size: 14px;
  line-height: 1.4;
`;

const ModelSelect = styled.select`
  width: 200px;
  padding: 0.5rem;
  margin-bottom: 1rem;
  position: absolute;
  top: 1rem;
  left: 1rem;
`;

const ChatLayout = styled.div`
  width: 800px;
  margin: 0 auto;
  height: 100vh;
  display: flex;
  flex-direction: column;
`;

const MessagesContainer = styled.div`
  height: 85vh;
  width: 95%;
  overflow-y: auto;
  padding: 1rem;
  scroll-behavior: smooth;
`;

const InputContainer = styled.div`
  height: 10vh;
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin: 1rem 3.3rem 4rem 0;
  background: white;
  border: 1px solid rgb(206, 206, 206);
  border-radius: 12px;
  &:focus {
    border-color: rgb(82, 87, 96);
  }
`;

const MessageInput = styled.div`
  flex: 1;
  padding: 0.6rem;
  outline: none;
  font-size: 14px;
  line-height: 1.4;
  font-family: inherit;
  white-space: pre-wrap;
  overflow-y: auto;
  cursor: text;
  word-break: break-word;
  color: #333;
  user-select: text;
  -webkit-user-select: text;
  contenteditable: true;
`;

const MessageContent = styled.div`
  flex: 1;
  word-break: break-word;
  white-space: pre-wrap;
`;

const SendButtonContainer = styled.div`
  display: flex;
  align-items: flex-end;
  align-self: end;
  padding: 0.2rem;
`;

const SendButton = styled.button`
  width: 25px;
  height: 25px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgb(213, 213, 213);
  border: none;
  border-radius: 12px;
  cursor: pointer;
  color: '#FFFFFF';
  
  &:hover {
    background: rgb(186, 186, 186);
  }
`;

const EditActions = styled.div`
  display: flex;
  gap: 8px;
  margin-top: 8px;
  justify-content: flex-end;
`;

const MessageActionsContainer = styled.div`
  position: absolute;
  right: 0;
  bottom: -28px;
  display: flex;
  gap: 4px;
  opacity: 0;
  transition: opacity 0.2s;
  
  ${Message}:hover & {
    opacity: 1;
  }
`;


const ActionButton = styled.button`
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: transparent;
  border: none;
  cursor: pointer;
  border-radius: 50%;
  color: #666;
  
  &:hover {
    background: rgba(0, 0, 0, 0.1);
  }
`;

const InlineMessageInput = styled.textarea`
  width: 100%;
  background: transparent;
  color: inherit;
  border: none;
  outline: none;
  resize: none;
  font-size: 14px;
  line-height: 1.4;
  font-family: inherit;
  min-height: 20px;
  overflow: hidden;
  
  &::placeholder {
    color: rgba(255, 255, 255, 0.7);
  }
`;

interface ChatMessage {
  id: string; // Уникальный идентификатор сообщения
  role: 'user' | 'assistant'; // Роль отправителя - пользователь или ассистент
  content: string; // Содержание сообщения
  timestamp: number; // Временная метка создания сообщения
}

const Chat: React.FC = () => {
  const { chatId } = useParams();
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [isGenerating, setIsGenerating] = useState(false);
  const [currentChatId, setCurrentChatId] = useState<string | null>(null);
  const [editingMessageId, setEditingMessageId] = useState<string | null>(null);
  const [message, setMessage] = useState<string>('');
  const dispatch = useDispatch();
  const { models, selectedModel } = useSelector((state: RootState) => state.models);
  const [editingContent, setEditingContent] = useState<string>('');
  const messagesEndRef = useRef<HTMLDivElement>(null);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  };

  // Загрузка моделей при монтировании компонента
  useEffect(() => {
    const loadModels = async () => {
      const modelsData = await fetchModels();
      dispatch(setModels(modelsData));

      // Установка модели по умолчанию
      const defaultModel = modelsData.find(model => model.isDefault);
      if (defaultModel) {
        dispatch(setSelectedModel(defaultModel));
      }
    };

    loadModels();
  }, [dispatch]);

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  useEffect(() => {
    if (chatId) {
      const loadChatHistory = async () => {
        const chat = await fetchChats();
        const currentChat = chat.data.find((c: ChatType) => c.id === Number(chatId));
        if (currentChat) {
          setCurrentChatId(currentChat.id.toString());  // Устанавливаем ID текущего чата
          const formattedMessages = currentChat.messages.map((msg: MessageType) => ({
            id: String(msg.id),
            role: msg.messageType === 'REQUEST' ? 'user' : 'assistant',
            content: msg.content,
            timestamp: new Date(msg.createdAt).getTime()
          }));
          setMessages(formattedMessages);
        }
      };
      loadChatHistory();
    }
  }, [chatId]);

  const copyToClipboard = async (content: string) => {
    await navigator.clipboard.writeText(content);
    // Здесь можно добавить toast уведомление "Скопировано в буфер обмена"
  };

  const sendMessage = async (messageContent: string) => {
    if (!messageContent.trim() || !selectedModel) return;
    setIsGenerating(true);

    try {
      // Create chat and get chatId
      const chatResponse = await createChat(messageContent, selectedModel.id);
      const newChat = chatResponse.data;

      // Update store with new chat
      dispatch(addChat(newChat));
      setCurrentChatId(newChat.id);

      // Start generation
      const eventSource = generateResponse(newChat.id);
      let accumulatedResponse = '';

      eventSource.onmessage = (event) => {
        const chunk = JSON.parse(event.data);
        accumulatedResponse += chunk.content;

        setMessages(prev => {
          const newMessages = [...prev];
          const lastMessage = newMessages[newMessages.length - 1];
          if (lastMessage?.role === 'assistant') {
            lastMessage.content = accumulatedResponse;
          } else {
            newMessages.push({
              id: crypto.randomUUID(),
              role: 'assistant',
              content: accumulatedResponse,
              timestamp: Date.now()
            });
          }
          return newMessages;
        });
      };

      eventSource.onerror = () => {
        eventSource.close();
        setIsGenerating(false);
      };

    } catch (error) {
      console.error('Error:', error);
      setIsGenerating(false);
    }
  };


  const deleteMessage = async (messageId: string) => {
    try {
      await fetch(`/api/messages/${messageId}`, {
        method: 'DELETE'
      });

      setMessages(prev => prev.filter(msg => msg.id !== messageId));
    } catch (error) {
      console.error('Error deleting message:', error);
    }
  };

  // Обработчик начала редактирования
  const handleEdit = (messageId: string, content: string) => {
    setEditingMessageId(messageId);
    setEditingContent(content);
  };

  const saveEdit = async (messageId: string) => {
    if (!editingContent.trim()) return;

    const updatedMessages = messages.map(msg =>
      msg.id === messageId ? { ...msg, content: editingContent } : msg
    );

    setMessages(updatedMessages);
    setEditingMessageId(null);
    setEditingContent('');

    // Здесь можно добавить API запрос для сохранения изменений
  };

  // Отмена редактирования
  const cancelEdit = () => {
    setEditingMessageId(null);
    setMessage('');
  };

  const fetchSummarization = async (text: string, mode: "short" | "long"): Promise<string> => {
    try {
      const response = await axios.post<string>(
        "http://localhost:8000/summarize",
        { text, mode },
        { headers: { "Content-Type": "application/json" } }
      );
      return response.data;
    } catch (error: any) {
      console.error("Summarization request failed:", error);
      throw error;
    }
  };

  // Обработчик отправки сообщения или сохранения редактирования
  const handleSubmit = () => {
    if (message.trim()) {
      sendMessage(message);
      setMessage('');
      // Clear the contentEditable div
      const inputElement = document.querySelector('[role="textbox"]');
      if (inputElement) {
        inputElement.textContent = '';
      }
    }
    // fetchSummarization(message, "short");
  };

  // Обработчик изменения выбранной модели
  const handleModelChange = (modelId: number) => {
    const selectedModel = models.find(model => model.id === modelId);
    if (selectedModel) {
      dispatch(setSelectedModel(selectedModel));
    }
  };

  const handleKeyDown = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSubmit();
    }
    if (e.key === 'Escape' && editingMessageId) {
      cancelEdit();
    }
  };

  const stopGeneration = () => {
    if (currentChatId) {
      fetch(`/api/chat/${currentChatId}/stop`, { method: 'POST' });
      setIsGenerating(false);
    }
  };

  const regenerateResponse = async (messageId: string) => {
    console.log("Starting regeneration...");
    console.log("Current state:", { selectedModel, currentChatId, messageId });

    if (!selectedModel || !currentChatId) {
      console.log("Stopping due to missing model or chatId");
      return;
    }

    setIsGenerating(true);

    try {
      // Get the message content for regeneration
      const messageToRegenerate = messages.find(m => m.id === messageId);
      if (!messageToRegenerate) return;

      // Remove all messages after the selected message
      const messageIndex = messages.findIndex(m => m.id === messageId);
      setMessages(messages.slice(0, messageIndex + 1));

      // Start generation with SSE
      const eventSource = generateResponse(currentChatId, messageId);
      let accumulatedResponse = '';

      eventSource.onmessage = (event) => {
        const chunk = event.data;
        accumulatedResponse += chunk;

        setMessages(prev => {
          const newMessages = [...prev];
          const lastMessage = newMessages[newMessages.length - 1];
          if (lastMessage?.role === 'assistant') {
            lastMessage.content = accumulatedResponse;
          } else {
            newMessages.push({
              id: crypto.randomUUID(),
              role: 'assistant',
              content: accumulatedResponse,
              timestamp: Date.now()
            });
          }
          return newMessages;
        });
      };


      eventSource.onerror = () => {
        eventSource.close();
        setIsGenerating(false);
      };
    } catch (error) {
      console.error('Regeneration error:', error);
      setIsGenerating(false);
    }
  };


  return (
    <ChatContainer>
      <ModelSelect
        value={selectedModel?.id || ''}
        onChange={(e) => handleModelChange(Number(e.target.value))}
        title={selectedModel?.description}
      >
        <option value="">Выберите модель</option>
        {models.map((model) => (
          <option key={model.id} value={model.id}>
            {model.name}
          </option>
        ))}
      </ModelSelect>

      <ChatLayout>
        <MessagesContainer>
          {messages.map((msg, index) => (
            <Message key={msg.id} isUser={msg.role === 'user'}>
              {editingMessageId === msg.id ? (
                <>
                  <InlineMessageInput
                    value={editingContent}
                    onChange={(e) => {
                      setEditingContent(e.target.value);
                      // Автоматическое изменение высоты
                      e.target.style.height = 'auto';
                      e.target.style.height = e.target.scrollHeight + 'px';
                    }}
                    autoFocus
                    onKeyDown={(e) => {
                      if (e.key === 'Enter' && !e.shiftKey) {
                        e.preventDefault();
                        saveEdit(msg.id);
                      }
                      if (e.key === 'Escape') {
                        setEditingMessageId(null);
                        setEditingContent('');
                      }
                    }}
                  />
                  <EditActions>
                    <ActionButton onClick={() => saveEdit(msg.id)} title="Сохранить">
                      <Check size={14} />
                    </ActionButton>
                    <ActionButton
                      onClick={() => {
                        setEditingMessageId(null);
                        setEditingContent('');
                      }}
                      title="Отменить"
                    >
                      <X size={14} />
                    </ActionButton>
                  </EditActions>
                </>
              ) : (
                <>
                  <MessageContent>{msg.content}</MessageContent>
                  {msg.role === 'user' && (
                    <MessageActionsContainer>
                      <ActionButton
                        onClick={() => {
                          console.log("Regenerate clicked for message:", msg.id);
                          regenerateResponse(msg.id);
                        }}
                        title="Повторить генерацию"
                      >
                        <Repeat size={14} />
                      </ActionButton>
                      <ActionButton onClick={() => handleEdit(msg.id, msg.content)} title="Редактировать">
                        <Edit size={14} />
                      </ActionButton>
                      <ActionButton onClick={() => copyToClipboard(msg.content)} title="Копировать">
                        <Copy size={14} />
                      </ActionButton>
                      {index > 0 && (
                        <ActionButton onClick={() => deleteMessage(msg.id)} title="Удалить">
                          <Trash2 size={14} />
                        </ActionButton>
                      )}
                    </MessageActionsContainer>
                  )}
                </>
              )}
            </Message>
          ))}

        </MessagesContainer>
        <InputContainer>
          <MessageInput
            contentEditable
            onInput={(e: React.FormEvent<HTMLDivElement>) => {
              setMessage(e.currentTarget.textContent || '');
            }}
            onKeyDown={handleKeyDown}
            role="textbox"
            aria-multiline="true"
            data-placeholder="Enter message..."
          />
          <SendButtonContainer>
            {isGenerating ? (
              <SendButton onClick={stopGeneration}>
                <StopCircle size={16} color="rgb(118, 118, 120)" />
              </SendButton>
            ) : (
              <SendButton onClick={handleSubmit}>
                <Send size={16} color="rgb(118, 118, 120)" />
              </SendButton>
            )}
          </SendButtonContainer>
        </InputContainer>
      </ChatLayout>
    </ChatContainer>
  );
};

export default Chat;
