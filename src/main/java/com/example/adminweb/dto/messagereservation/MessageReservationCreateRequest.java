package com.example.adminweb.dto.messagereservation;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MessageReservationCreateRequest {

  private Long userGroupId;
  private Long templateId;
  private LocalDateTime scheduledAt;
}