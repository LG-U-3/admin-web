package com.example.adminweb.service;

import com.example.adminweb.common.code.CodeCache;
import com.example.adminweb.common.code.enums.CodeGroups;
import com.example.adminweb.common.code.enums.MessagePurpose;
import com.example.adminweb.common.code.enums.MessageSendStatus;
import com.example.adminweb.config.producer.MessageStreamProducer;
import com.example.adminweb.repository.MessageSendResultRepository;
import com.example.adminweb.repository.projection.MessagePublishProjection;
import com.example.adminweb.repository.projection.MessageRetryViewProjection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MessageService {

  private final MessageSendResultRepository repository;
  private final CodeCache codeCache;
  private final MessageStreamProducer messageStreamProducer;

  @Transactional
  public void retrySelected(List<Long> messageSendResultIds) {

    if (messageSendResultIds == null || messageSendResultIds.isEmpty()) {
      return;
    }

    Long failedStatusId =
        codeCache.getId(CodeGroups.MESSAGE_SEND_STATUS, MessageSendStatus.FAILED);
    Long waitingStatusId =
        codeCache.getId(CodeGroups.MESSAGE_SEND_STATUS, MessageSendStatus.WAITING);
    Long billingPurposeTypeId =
        codeCache.getId(CodeGroups.MESSAGE_PURPOSE, MessagePurpose.BILLING);

    // 재처리 중복 방지를 위해 FAILED -> WAITING으로 변경
    // retry 횟수 1 증가 / Consumer에서 WAITING -> PROCESSING으로 처리
    repository.retryToWaiting(
        messageSendResultIds,
        waitingStatusId,
        failedStatusId,
        billingPurposeTypeId
    );

    // WAITING 상태로 변경된 것 Publish
    List<MessagePublishProjection> publishTargets =
        repository.findPublishTargets(
            messageSendResultIds,
            waitingStatusId
        );

    for (MessagePublishProjection target : publishTargets) {
      messageStreamProducer.publish(
          target.getMessageSendResultId(),
          target.getChannelCode(),
          target.getPurposeCode()
      );
    }
  }

  @Transactional(readOnly = true)
  public Page<MessageRetryViewProjection> findRetryableMessages(
      Pageable pageable
  ) {
    Long failedStatusId =
        codeCache.getId(CodeGroups.MESSAGE_SEND_STATUS, MessageSendStatus.FAILED);

    Long billingPurposeTypeId =
        codeCache.getId(CodeGroups.MESSAGE_PURPOSE, MessagePurpose.BILLING);

    return repository.findRetryableMessages(
        failedStatusId,
        billingPurposeTypeId,
        pageable
    );
  }
}
