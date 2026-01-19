package com.example.adminweb.service;

import com.example.adminweb.common.code.CodeCache;
import com.example.adminweb.common.code.enums.CodeGroups;
import com.example.adminweb.common.code.enums.MessageSendStatus;
import com.example.adminweb.common.code.enums.ReservationStatus;
import com.example.adminweb.domain.code.Code;
import com.example.adminweb.domain.message.MessageReservation;
import com.example.adminweb.domain.message.MessageSendResult;
import com.example.adminweb.producer.MessageStreamProducer;
import com.example.adminweb.repository.MessageReservationRepository;
import com.example.adminweb.repository.MessageSendResultRepository;
import com.example.adminweb.repository.UserUserGroupRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@RequiredArgsConstructor
public class MessageRequestService {

  private final MessageSendResultRepository messageSendResultRepository;
  private final MessageReservationRepository messageReservationRepository;
  private final UserUserGroupRepository userUserGroupRepository;
  private final MessageStreamProducer streamProducer;

  private final CodeCache codeCache;

  private Long STATUS_SENT;

  @EventListener(ApplicationReadyEvent.class)
  void initStatuses() {
    this.STATUS_SENT =
        codeCache.getId(CodeGroups.RESERVATION_STATUS, ReservationStatus.SENT);
  }

  @Transactional
  public void requestMessage(Long messageReservationId) {
    markProcessing(messageReservationId);
    MessageReservation reservation =
        messageReservationRepository.findByIdWithRelations(messageReservationId)
            .orElseThrow();

    String channelTypeCode = reservation.getTemplate().getChannelType().getCode();
    String purposeTypeCode = reservation.getTemplate().getPurposeType().getCode();

    Long userGroupId = reservation.getUserGroup().getId();
    List<Long> userIds = userUserGroupRepository.findUserIdsByGroupId(userGroupId);

    Code waitingStatus = codeCache.get(
        CodeGroups.MESSAGE_SEND_STATUS,
        MessageSendStatus.WAITING
    );

    List<Long> resultIds = new ArrayList<>(); // 이후 메시지 발송을 위해 발송 상태ID 저장

    for (Long userId : userIds) {
      MessageSendResult result = messageSendResultRepository.save(
          MessageSendResult.createFrom(reservation, userId, waitingStatus)
      );
      resultIds.add(result.getId());
    }

    TransactionSynchronizationManager.registerSynchronization(
        new TransactionSynchronization() {
          @Override
          public void afterCommit() {
            for (Long resultId : resultIds) {
              streamProducer.publish(resultId, channelTypeCode, purposeTypeCode);
            }
          }
        }
    );

    markSent(reservation.getId());
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW) // 즉시 커밋
  public Long markProcessing(Long messageReservationId) {
    MessageReservation reservation =
        messageReservationRepository.findByIdWithStatus(messageReservationId)
            .orElseThrow(() -> new IllegalArgumentException("예약 발송 없음"));

    if (!"WAITING".equals(reservation.getStatus().getCode())) {
      throw new IllegalArgumentException("이미 처리된 예약 발송입니다.");
    }

    if (reservation.getScheduledAt().isAfter(LocalDateTime.now())) {
      throw new IllegalArgumentException("아직 처리할 수 없는 예약 발송입니다.");
    }

    reservation.setStatus(
        codeCache.get(CodeGroups.RESERVATION_STATUS, ReservationStatus.PROCESSING)
    );

    return reservation.getId();
  }

  @Transactional
  public void markSent(Long reservationId) {
    messageReservationRepository.updateStatus(
        reservationId,
        STATUS_SENT
    );
  }
}
