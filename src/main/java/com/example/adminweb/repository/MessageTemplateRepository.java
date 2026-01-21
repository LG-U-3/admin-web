package com.example.adminweb.repository;

import com.example.adminweb.domain.message.MessageTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MessageTemplateRepository extends JpaRepository<MessageTemplate, Long>,
    JpaSpecificationExecutor<MessageTemplate> {

}
