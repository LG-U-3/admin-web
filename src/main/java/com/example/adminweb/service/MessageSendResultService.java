package com.example.adminweb.service;

import com.example.adminweb.domain.message.MessageSendResult;
import com.example.adminweb.dto.message.MessageHistorySearch;
import com.example.adminweb.dto.message.MessageSendResultResponse;
import com.example.adminweb.repository.MessageSendResultRepository;
import com.example.adminweb.repository.spec.MessageSendResultSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageSendResultService {

  private final MessageSendResultRepository messageSendResultRepository;

  public Page<MessageSendResultResponse> search(
      MessageHistorySearch search,
      Pageable pageable
  ) {

    Specification<MessageSendResult> spec =
        MessageSendResultSpec.channelCode(search.getChannelCode())
            .and(MessageSendResultSpec.statusCode(search.getStatusCode()))
            .and(MessageSendResultSpec.userId(search.getUserId()))
            .and(MessageSendResultSpec.templateName(search.getTemplateName()))
            .and(MessageSendResultSpec.requestedBetween(
                search.getStartDate(),
                search.getEndDate()
            ));

    return messageSendResultRepository.findAll(spec, pageable)
        .map(this::toDto);
  }

  private MessageSendResultResponse toDto(MessageSendResult entity) {
    return MessageSendResultResponse.builder()
        .messageId(entity.getId())
        .userId(entity.getUserId())
        .channelCode(entity.getChannel().getCode())
        .templateName(entity.getTemplate().getName())
        .statusCode(entity.getStatus().getCode())
        .retryCount(entity.getRetryCount())
        .requestedAt(entity.getRequestedAt())
        .processedAt(entity.getProcessedAt())
        .displayTime(
            entity.getProcessedAt() != null
                ? entity.getProcessedAt()
                : entity.getRequestedAt()
        )
        .build();
  }
}
