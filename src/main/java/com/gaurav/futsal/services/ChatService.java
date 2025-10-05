package com.gaurav.futsal.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gaurav.futsal.entity.ConversationEntity;
import com.gaurav.futsal.entity.MessageEntity;
import com.gaurav.futsal.repository.MessageRepository;

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
