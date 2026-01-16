package com.example.adminweb.controller;

import com.example.adminweb.service.MessageRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {

  private final MessageRequestService messageRequestService;

  @PostMapping("/reservations/{reservationId}/send")
  public ResponseEntity<Void> requestMessage(
      @PathVariable Long reservationId
  ) {
    messageRequestService.requestMessage(reservationId);
    return ResponseEntity.ok().build();
  }
}
