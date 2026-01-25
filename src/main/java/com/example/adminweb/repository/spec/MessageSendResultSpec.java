package com.example.adminweb.repository.spec;

import com.example.adminweb.domain.message.MessageSendResult;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.data.jpa.domain.Specification;

public class MessageSendResultSpec {

  public static Specification<MessageSendResult> channelCode(String channelCode) {
    return (root, query, cb) ->
        (channelCode == null || channelCode.isBlank())
            ? null
            : cb.equal(root.get("channel").get("code"), channelCode);
  }

  public static Specification<MessageSendResult> statusCode(String statusCode) {
    return (root, query, cb) ->
        (statusCode == null || statusCode.isBlank())
            ? null
            : cb.equal(root.get("status").get("code"), statusCode);
  }

  public static Specification<MessageSendResult> userId(Long userId) {
    return (root, query, cb) ->
        userId == null
            ? null
            : cb.equal(root.get("userId"), userId);
  }

  public static Specification<MessageSendResult> templateName(String templateName) {
    return (root, query, cb) ->
        (templateName == null || templateName.isBlank())
            ? null
            : cb.like(
                root.get("template").get("name"),
                "%" + templateName + "%"
            );
  }

  public static Specification<MessageSendResult> requestedBetween(
      LocalDate startDate,
      LocalDate endDate
  ) {
    return (root, query, cb) -> {
      if (startDate == null && endDate == null) {
        return null;
      }

      LocalDateTime start =
          startDate != null ? startDate.atStartOfDay() : LocalDateTime.MIN;
      LocalDateTime end =
          endDate != null ? endDate.plusDays(1).atStartOfDay() : LocalDateTime.MAX;

      return cb.between(root.get("requestedAt"), start, end);
    };
  }
}
