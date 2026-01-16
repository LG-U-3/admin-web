package com.example.adminweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.adminweb.domain.message.MessageTemplate;

public interface MessageTemplateRepository extends JpaRepository<MessageTemplate, Long> {
}
