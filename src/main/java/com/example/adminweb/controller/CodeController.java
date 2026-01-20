package com.example.adminweb.controller;

import com.example.adminweb.dto.code.CodeGroupResponse;
import com.example.adminweb.service.CodeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/code-groups")
public class CodeController {

  private final CodeService codeService;

  @GetMapping
  public List<CodeGroupResponse> getCodeGroups() {
    return codeService.getCodeGroupsWithCodes();
  }
}