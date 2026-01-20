package com.example.adminweb.dto.code;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CodeResponse {

  private Long id;
  private String code;
  private String name;
}
