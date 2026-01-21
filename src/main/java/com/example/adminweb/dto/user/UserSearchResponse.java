package com.example.adminweb.dto.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSearchResponse {

  private Long userId;
  private String name;
}
