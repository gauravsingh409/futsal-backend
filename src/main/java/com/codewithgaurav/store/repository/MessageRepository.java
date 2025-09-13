package com.codewithgaurav.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.codewithgaurav.store.entity.MessageEntity;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
}
