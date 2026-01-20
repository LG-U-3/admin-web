package com.example.adminweb.dto.message;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MessageTemplateListResponse {

  private Long id;
  private String code;
  private String name;

  private Long channelTypeId;
  private String channelTypeCode;

  private Long purposeTypeId;
  private String purposeTypeCode;

  private String title;
}