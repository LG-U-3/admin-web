package com.example.adminweb.controller;

import com.example.adminweb.service.MessageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/messages")
@RequiredArgsConstructor
public class MessageController {

  private final MessageService messageService;

  @PostMapping("/retry")
  public ResponseEntity<Void> retrySelected(
      @RequestBody List<Long> messageSendResultIds
  ) {
    if (messageSendResultIds == null || messageSendResultIds.isEmpty()) {
      return ResponseEntity.ok().build();
    }
    messageService.retrySelected(messageSendResultIds);
    return ResponseEntity.ok().build();
  }
}
