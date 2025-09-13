package com.codewithgaurav.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.codewithgaurav.store.entity.ConversationEntity;

public interface ConversationRepository extends JpaRepository<ConversationEntity, Long> {
}
