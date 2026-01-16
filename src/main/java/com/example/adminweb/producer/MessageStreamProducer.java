package com.example.adminweb.producer;

import java.util.Map;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.stereotype.Component;

@Component
public class MessageStreamProducer {

  private static final String STREAM_KEY = "message-stream";

  private final StreamOperations<String, String, String> streamOps;

  public MessageStreamProducer(StreamOperations<String, String, String> streamOps) {
    this.streamOps = streamOps;
  }

  public void publish(Long messageSendResultId, String channel, String purpose) {
    streamOps.add(
        STREAM_KEY,
        Map.of(
            "messageSendResultId", messageSendResultId.toString(),
            "channel", channel,
            "purpose", purpose
        )
    );
  }
}