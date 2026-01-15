package com.example.adminweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.adminweb.domain.message.MessageSendResult;

public interface MessageSendResultRepository extends JpaRepository<MessageSendResult, Long> {
}
