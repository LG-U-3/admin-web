package com.example.adminweb.dto.message;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageTemplateCreateRequest {

  private String code;
  private String name;
  private Long channelTypeId;
  private Long purposeTypeId;
  private String title;
  private String body;
  private String variablesJson;
}