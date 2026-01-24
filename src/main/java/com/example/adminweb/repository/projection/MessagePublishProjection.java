package com.example.adminweb.repository.projection;

public interface MessagePublishProjection {

  Long getMessageSendResultId();

  String getChannelCode();

  String getPurposeCode();
}
