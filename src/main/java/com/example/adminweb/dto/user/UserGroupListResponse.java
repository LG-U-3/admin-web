package com.example.adminweb.dto.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserGroupListResponse {

  private Long groupId;
  private String code;
  private String name;
  private long userCount;
}
