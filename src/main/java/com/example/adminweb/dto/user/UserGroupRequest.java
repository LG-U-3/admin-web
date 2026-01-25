package com.example.adminweb.dto.user;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserGroupRequest {

  private String code;
  private String name;
  private String description;
  private List<Long> userIds;
}