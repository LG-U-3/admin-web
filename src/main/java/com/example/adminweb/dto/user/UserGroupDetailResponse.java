package com.example.adminweb.dto.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserGroupDetailResponse {

  private Long groupId;
  private String code;
  private String groupName;
  private String description;
}

