package com.gaurav.futsal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gaurav.futsal.entity.ConversationEntity;

public interface ConversationRepository extends JpaRepository<ConversationEntity, Long> {
}
