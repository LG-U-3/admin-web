package com.example.adminweb.controller;

import com.example.adminweb.dto.message.MessageTemplateCreateRequest;
import com.example.adminweb.dto.message.MessageTemplateListResponse;
import com.example.adminweb.service.CodeService;
import com.example.adminweb.service.MessageTemplateService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/admin/message-templates")
public class MessageTemplateController {

  private final MessageTemplateService messageTemplateService;
  private final CodeService codeService;

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

  @GetMapping("/{code}/edit")
  public String editForm(@PathVariable String code, Model model) {
    model.addAttribute("templateRequest", messageTemplateService.getTemplateForEdit(code));

    model.addAttribute("channelTypes", codeService.getChannelTypes());
    model.addAttribute("purposeTypes", codeService.getPurposeTypes());

    return "admin/template-edit";
  }

  @PostMapping("/{code}/edit")
  public void updateTemplate(
      @PathVariable String code,
      @ModelAttribute MessageTemplateCreateRequest request,
      HttpServletResponse response
  ) throws IOException {
    messageTemplateService.updateTemplate(code, request);
    response.sendRedirect("/admin/templates");
  }

}
