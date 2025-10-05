package com.gaurav.futsal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gaurav.futsal.entity.ConversationEntity;
import com.gaurav.futsal.entity.MessageEntity;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    List<MessageEntity> findByConversationOrderByCreatedAtAsc(ConversationEntity conversation);
}
