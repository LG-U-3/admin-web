package com.example.adminweb.repository;

import com.example.adminweb.domain.message.MessageTemplate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MessageTemplateRepository extends JpaRepository<MessageTemplate, Long>,
    JpaSpecificationExecutor<MessageTemplate> {

  Optional<MessageTemplate> findByCode(String code);
}
