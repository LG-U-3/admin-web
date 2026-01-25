package com.example.adminweb.repository.projection;

public interface MessageRetryViewProjection {

  Long getMessageSendResultId();

  String getUserId();

  String getChannelCode();

  String getTemplateCode();

  String getStatusCode();

  Integer getRetryCount();
}
