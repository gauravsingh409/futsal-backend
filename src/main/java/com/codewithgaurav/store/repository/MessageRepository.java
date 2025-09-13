package com.codewithgaurav.store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codewithgaurav.store.entity.ConversationEntity;
import com.codewithgaurav.store.entity.MessageEntity;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    List<MessageEntity> findByConversationOrderByCreatedAtAsc(ConversationEntity conversation);
}
