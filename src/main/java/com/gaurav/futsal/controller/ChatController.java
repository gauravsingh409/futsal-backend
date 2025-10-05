package com.gaurav.futsal.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.gaurav.futsal.entity.MessageEntity;
import com.gaurav.futsal.services.ChatService;

@Controller
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    public ChatController(SimpMessagingTemplate messagingTemplate, ChatService chatService) {
        this.messagingTemplate = messagingTemplate;
        this.chatService = chatService;
    }

    @MessageMapping("/chat.sendMessage") // Similar to post mapping in rest, but for web STOMP message
    public void sendMessage(MessageEntity message) {
        System.out.println("received");
        // MessageEntity savedMessage = chatService.saveMessage(message);
        // messagingTemplate.convertAndSend(
        //         "/topic/conversations/" + message.getConversation().getId(),
        //         savedMessage);
    }

}
