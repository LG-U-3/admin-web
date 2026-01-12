package com.example.adminweb.message;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer {

  private final StringRedisTemplate redisTemplate;

  public MessageProducer(StringRedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  public void sendEmail(String to, String title, String content) {
    redisTemplate.opsForStream().add(
        "message-stream",
        Map.of(
            "messageId", UUID.randomUUID().toString(),
            "channel","EMAIL",
            "to", to,
            "title", title,
            "content", content,
            "requestedAt", LocalDateTime.now().toString()
        )
    );
  }

  public void sendSms(String to, String content) {
    redisTemplate.opsForStream().add(
        "message-stream",
        Map.of(
            "messageId", UUID.randomUUID().toString(),
            "channel","SMS",
            "to", to,
            "content", content,
            "requestedAt", LocalDateTime.now().toString()
        )
    );
  }
}
