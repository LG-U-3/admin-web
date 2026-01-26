package com.example.adminweb.dto.message;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MessageSendResultResponse {

  private Long messageId;
  private Long userId;

  private String channelCode;
  private String templateName;

  private String statusCode;
  private int retryCount;

  private LocalDateTime requestedAt;
  private LocalDateTime processedAt;

  private LocalDateTime displayTime;
}
