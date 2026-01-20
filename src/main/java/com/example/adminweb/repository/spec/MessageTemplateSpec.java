package com.example.adminweb.repository.spec;

import com.example.adminweb.domain.message.MessageTemplate;
import org.springframework.data.jpa.domain.Specification;

public class MessageTemplateSpec {

  public static Specification<MessageTemplate> channelType(Long channelTypeId) {
    return (root, query, cb) ->
        channelTypeId == null ? null :
            cb.equal(root.get("channelType").get("id"), channelTypeId);
  }

  public static Specification<MessageTemplate> purposeType(Long purposeTypeId) {
    return (root, query, cb) ->
        purposeTypeId == null ? null :
            cb.equal(root.get("purposeType").get("id"), purposeTypeId);
  }

  public static Specification<MessageTemplate> keyword(String keyword) {
    return (root, query, cb) ->
        (keyword == null || keyword.isBlank()) ? null :
            cb.or(
                cb.like(root.get("code"), "%" + keyword + "%"),
                cb.like(root.get("name"), "%" + keyword + "%")
            );
  }
}