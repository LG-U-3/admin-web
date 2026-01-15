package com.example.adminweb.controller;

import org.springframework.web.bind.annotation.*;
import com.example.adminweb.producer.MessageProducer;

@RestController
@RequestMapping("/message")
public class MessageController {

  private final MessageProducer producer;

  public MessageController(MessageProducer producer) {
    this.producer = producer;
  }

  @PostMapping("/email")
  public String sendEmail() {
    producer.sendEmail(
        "test@example.com",
        "Mock Email",
        "이메일 발송 테스트"
    );
    return "EMAIL queued";
  }

  @PostMapping("/sms")
  public String sendSms() {
    producer.sendSms(
        "01012345678",
        "SMS 발송 테스트"
    );
    return "SMS queued";
  }
}
