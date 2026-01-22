package com.example.adminweb.dto.messagereservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MessageReservationListResponse {

  private Long reservationId;
  private String userGroupName;
  private String templateName;
  private LocalDateTime scheduledAt;
  private String status;   // WAITING / SENT / CANCELED
}
