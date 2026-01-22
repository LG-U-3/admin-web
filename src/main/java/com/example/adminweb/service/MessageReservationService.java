package com.example.adminweb.service;

import com.example.adminweb.domain.code.Code;
import com.example.adminweb.domain.message.MessageReservation;
import com.example.adminweb.dto.messagereservation.MessageReservationListResponse;
import com.example.adminweb.repository.CodeRepository;
import com.example.adminweb.repository.MessageReservationQueryRepository;

import com.example.adminweb.repository.MessageReservationRepository;
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

    MessageReservation reservation = reservationRepository
        .findByIdWithStatus(reservationId)
        .orElseThrow(() -> new IllegalArgumentException("예약 없음"));

    // 이미 완료/취소된 건 취소 불가
    if (!"WAITING".equals(reservation.getStatus().getCode())) {
      throw new IllegalStateException("대기 상태만 취소 가능합니다.");
    }

    Code canceledStatus = codeRepository.findByCode("CANCELED")
        .orElseThrow(() -> new IllegalStateException("CANCELED 코드 없음"));

    reservation.setStatus(canceledStatus);
  }
}
