package com.example.adminweb.controller;

import com.example.adminweb.dto.messagereservation.MessageReservationCreateRequest;
import com.example.adminweb.dto.messagereservation.MessageReservationFormResponse;
import com.example.adminweb.dto.messagereservation.MessageReservationListResponse;
import com.example.adminweb.service.MessageReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/message-reservations")
@RequiredArgsConstructor
public class MessageReservationController {

  private final MessageReservationService service;

  @GetMapping
  public Page<MessageReservationListResponse> list(
      @RequestParam(required = false) String status,
      @RequestParam(required = false) String templateName,
      @RequestParam(required = false) String groupName,
      Pageable pageable
  ) {
    return service.getReservations(
        status, templateName, groupName, pageable
    );
  }

  @PatchMapping("/{id}/cancel")
  public void cancel(@PathVariable Long id) {
    service.cancelReservation(id);
  }

  @GetMapping("/form")
  public MessageReservationFormResponse getFormData() {
    return service.getFormData();
  }

  @PostMapping
  public void create(@RequestBody MessageReservationCreateRequest request) {
    service.createReservation(request);
  }
}