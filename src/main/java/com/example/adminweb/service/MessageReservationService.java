package com.example.adminweb.service;

import com.example.adminweb.domain.code.Code;
import com.example.adminweb.domain.message.MessageReservation;
import com.example.adminweb.domain.message.MessageTemplate;
import com.example.adminweb.domain.user.UserGroup;
import com.example.adminweb.dto.message.MessageTemplateListResponse;
import com.example.adminweb.dto.messagereservation.MessageReservationCreateRequest;
import com.example.adminweb.dto.messagereservation.MessageReservationFormResponse;
import com.example.adminweb.dto.messagereservation.MessageReservationListResponse;
import com.example.adminweb.dto.user.UserGroupSimpleResponse;
import com.example.adminweb.repository.CodeRepository;
import com.example.adminweb.repository.MessageReservationQueryRepository;

import com.example.adminweb.repository.MessageReservationRepository;
import com.example.adminweb.repository.MessageTemplateRepository;
import com.example.adminweb.repository.UserGroupRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MessageReservationService {

  private final MessageReservationQueryRepository queryRepository;

  private final MessageReservationRepository reservationRepository;
  private final CodeRepository codeRepository;

  private final UserGroupRepository userGroupRepository;
  private final MessageTemplateRepository messageTemplateRepository;

  public Page<MessageReservationListResponse> getReservations(
      String status,
      String templateName,
      String groupName,
      Pageable pageable
  ) {
    return queryRepository.findReservations(
        status, templateName, groupName, pageable
    );
  }

  @Transactional
  public void cancelReservation(Long reservationId) {

    MessageReservation reservation = reservationRepository.findByIdWithStatus(reservationId)
        .orElseThrow(() -> new IllegalArgumentException("예약 없음"));

    // 이미 완료/취소된 건 취소 불가
    if (!"WAITING".equals(reservation.getStatus().getCode())) {
      throw new IllegalStateException("대기 상태만 취소 가능합니다.");
    }

    Code canceledStatus = codeRepository.findByCode("CANCELED")
        .orElseThrow(() -> new IllegalStateException("CANCELED 코드 없음"));

    reservation.setStatus(canceledStatus);
  }

  @Transactional
  public void createReservation(MessageReservationCreateRequest req) {

    if (req.getScheduledAt().isBefore(LocalDateTime.now())) {
      throw new IllegalArgumentException("과거 시간은 예약할 수 없습니다.");
    }

    // 1. 상태 코드 조회
    Code waitingStatus = codeRepository
        .findByCodeAndCodeGroup_Code("WAITING", "RESERVATION_STATUS")
        .orElseThrow(() -> new IllegalStateException("WAITING 상태 코드 없음"));

    // 2. 사용자 그룹 조회
    UserGroup userGroup = userGroupRepository.findById(req.getUserGroupId())
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 그룹"));

    // 3. 템플릿 조회
    MessageTemplate template = messageTemplateRepository.findById(req.getTemplateId())
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 템플릿"));

    // 4. 예약 생성
    MessageReservation reservation = MessageReservation.builder()
        .scheduledAt(req.getScheduledAt())
        .status(waitingStatus)
        .channelType(template.getChannelType()) // 템플릿 기준
        .template(template)
        .userGroup(userGroup)
        .build();

    // 5. 저장
    reservationRepository.save(reservation);
  }

  @Transactional(readOnly = true)
  public MessageReservationFormResponse getFormData() {

    // 1. 사용자 그룹 조회
    List<UserGroupSimpleResponse> userGroups =
        userGroupRepository.findAll().stream()
            .map(g -> new UserGroupSimpleResponse(
                g.getId(),
                g.getName()
            ))
            .toList();

    // 2. 메시지 템플릿 조회
    List<MessageTemplateListResponse> templates =
        messageTemplateRepository.findAll().stream()
            .filter(t -> !t.getPurposeType().getId().equals(14L))
            .map(t -> MessageTemplateListResponse.builder()
                .id(t.getId())
                .code(t.getCode())
                .name(t.getName())
                .channelTypeId(t.getChannelType().getId())
                .channelTypeCode(t.getChannelType().getCode())
                .purposeTypeId(t.getPurposeType().getId())
                .purposeTypeCode(t.getPurposeType().getCode())
                .title(t.getTitle())
                .build()
            )
            .toList();

    return new MessageReservationFormResponse(userGroups, templates);
  }
}
