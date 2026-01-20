package com.example.adminweb.dto.code;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CodeGroupResponse {

  private Long id;
  private String code;
  private String name;

  private List<CodeResponse> codes;
}