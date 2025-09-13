package com.codewithgaurav.store.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.codewithgaurav.store.entity.MessageEntity;

@Controller
public class ChatController {

    @MessageMapping("/chat.sendMessage")
    public MessageEntity sendMessage(MessageEntity messages) {
        return messages;
    }
}
