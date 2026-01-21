package com.example.adminweb.controller;

import com.example.adminweb.dto.message.MessageTemplateListResponse;
import com.example.adminweb.service.MessageTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/message-templates")
public class MessageTemplateController {

  private final MessageTemplateService messageTemplateService;

  @GetMapping
  public Page<MessageTemplateListResponse> list(
      @RequestParam(required = false) Long channelTypeId,
      @RequestParam(required = false) Long purposeTypeId,
      @RequestParam(required = false) String keyword,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size
  ) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
    return messageTemplateService.getTemplates(
        channelTypeId,
        purposeTypeId,
        keyword,
        pageable
    );
  }
}
