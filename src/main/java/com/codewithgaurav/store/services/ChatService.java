package com.codewithgaurav.store.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.codewithgaurav.store.entity.ConversationEntity;
import com.codewithgaurav.store.entity.MessageEntity;
import com.codewithgaurav.store.repository.MessageRepository;

@Service
public class ChatService {

    private final MessageRepository messageRepository;

    public ChatService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    // Save a message
    public MessageEntity saveMessage(MessageEntity message) {
        return messageRepository.save(message);
    }

    // Get all messages for a conversation
    public List<MessageEntity> getMessagesForConversation(ConversationEntity conversation) {
        return messageRepository.findByConversationOrderByCreatedAtAsc(conversation);
    }

}
