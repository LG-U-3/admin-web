package com.example.adminweb.controller;

import com.example.adminweb.dto.code.CodeGroupResponse;
import com.example.adminweb.dto.code.CodeResponse;
import com.example.adminweb.dto.message.MessageTemplateCreateRequest;
import com.example.adminweb.dto.message.MessageTemplateListResponse;
import com.example.adminweb.dto.user.UserGroupDetailResponse;
import com.example.adminweb.dto.user.UserGroupListResponse;
import com.example.adminweb.dto.user.UserGroupUserResponse;
import com.example.adminweb.service.CodeService;
import com.example.adminweb.service.MessageTemplateService;
import com.example.adminweb.service.UplusServiceService;
import com.example.adminweb.service.UserGroupService;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AdminPageController {

  private static final String MESSAGE_PURPOSE_GROUP_CODE = "MESSAGE_PURPOSE";
  private static final String MESSAGE_CHANNEL_GROUP_CODE = "MESSAGE_CHANNEL";

  private final UserGroupService userGroupService;
  private final MessageTemplateService messageTemplateService;
  private final CodeService codeService;
  private final UplusServiceService uplusServiceService;


  @GetMapping("/admin")
  public String demoRoot() {
    return "redirect:/admin/dashboard";
  }

  @GetMapping("/admin/dashboard")
  public String dashboard(Model model) {
    setPage(model, "Billing System Admin - 운영 현황 / 관리", "관리자 대시보드", "dashboard",
        "실시간 운영 요약");
    return "admin/dashboard";
  }

  @GetMapping("/admin/batch")
  public String batch(Model model) {
    setPage(model, "Billing System Admin - 정산 배치 실행/관리", "정산 배치 실행/관리", "batch",
        "월별 배치 실행 현황");
    return "admin/batch";
  }

  @GetMapping("/admin/settlement")
  public String settlement(Model model) {
    setPage(model, "Billing System Admin - 정산서 데이터 조회", "정산서 데이터 조회", "settlement",
        "정산 결과 검색");
    return "admin/settlement";
  }

  @GetMapping("/admin/messages/history")
  public String messageHistory(Model model) {
    setPage(model, "Billing System Admin - 메시지 발송 이력 조회", "메시지 발송 이력 조회",
        "message-history", "발송 이력 검색");
    return "admin/message-history";
  }

  @GetMapping("/admin/messages/retry")
  public String messageRetry(Model model) {
    setPage(model, "Billing System Admin - 메시지 재시도 관리", "메시지 재시도 관리",
        "message-retry", "실패 메시지 관리");
    return "admin/message-retry";
  }

  @GetMapping("/admin/templates")
  public String templates(
      @RequestParam(required = false) String channelTypeCode,
      @RequestParam(required = false) String purposeTypeCode,
      @RequestParam(required = false) String keyword,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      Model model
  ) {
    setPage(model, "Billing System Admin - 템플릿 목록", "템플릿 목록", "templates",
        "템플릿 관리");

    List<CodeGroupResponse> codeGroups = codeService.getCodeGroupsWithCodes();
    List<CodeResponse> channelTypes = getCodesByGroupCode(codeGroups, MESSAGE_CHANNEL_GROUP_CODE);
    List<CodeResponse> purposeTypes = getCodesByGroupCode(codeGroups, MESSAGE_PURPOSE_GROUP_CODE);

    Long channelTypeId = getCodeIdByCode(channelTypes, channelTypeCode);
    Long purposeTypeId = getCodeIdByCode(purposeTypes, purposeTypeCode);

    Page<MessageTemplateListResponse> templatePage =
        messageTemplateService.getTemplates(
            channelTypeId,
            purposeTypeId,
            keyword,
            PageRequest.of(page, size, Sort.by("id").descending())
        );

    long total = templatePage.getTotalElements();
    long start = total == 0 ? 0 : page * (long) size + 1;
    long end = total == 0 ? 0 : Math.min((page + 1L) * size, total);

    model.addAttribute("templates", templatePage.getContent());
    model.addAttribute("templatePage", templatePage);
    model.addAttribute("channelTypeCode", channelTypeCode);
    model.addAttribute("purposeTypeCode", purposeTypeCode);
    model.addAttribute("channelTypes", channelTypes);
    model.addAttribute("purposeTypes", purposeTypes);
    model.addAttribute("keyword", keyword);
    model.addAttribute("page", page);
    model.addAttribute("size", size);
    model.addAttribute("total", total);
    model.addAttribute("start", start);
    model.addAttribute("end", end);

    return "admin/templates";
  }

  @GetMapping("/admin/templates/new")
  public String templateCreate(Model model) {
    setPage(model, "Billing System Admin - 템플릿 등록", "템플릿 등록", "templates",
        "신규 템플릿 등록");

    List<CodeGroupResponse> codeGroups = codeService.getCodeGroupsWithCodes();
    model.addAttribute("channelTypes", getCodesByGroupCode(codeGroups, MESSAGE_CHANNEL_GROUP_CODE));
    model.addAttribute("purposeTypes", getCodesByGroupCode(codeGroups, MESSAGE_PURPOSE_GROUP_CODE));

    model.addAttribute("templateRequest", new MessageTemplateCreateRequest());
    return "admin/template-create";
  }

  @PostMapping("/admin/templates")
  public String createTemplate(
      @ModelAttribute("templateRequest") MessageTemplateCreateRequest request) {
    messageTemplateService.createTemplate(request);
    return "redirect:/admin/templates";
  }

  @GetMapping("/admin/templates/{templateId}/edit")
  public String templateEdit(Model model, @PathVariable("templateId") String templateId) {
    setPage(model, "Billing System Admin - 템플릿 수정", "템플릿 수정", "templates",
        "템플릿 편집 화면");
    return "admin/template-edit";
  }

  @GetMapping("/admin/schedule")
  public String scheduleList(Model model) {
    setPage(model, "Billing System Admin - 예약 발송 목록", "예약 발송 목록", "schedule-list",
        "예약 메시지 현황");
    return "admin/schedule-list";
  }

  @GetMapping("/admin/schedule/new")
  public String scheduleCreate(Model model) {
    setPage(model, "Billing System Admin - 예약 발송 등록", "예약 발송 등록",
        "schedule-list", "예약 발송 등록");
    return "admin/schedule-create";
  }

  @GetMapping("/admin/schedule/{scheduleId}")
  public String scheduleDetail(Model model, @PathVariable("scheduleId") String scheduleId) {
    setPage(model, "Billing System Admin - 예약 발송 상세", "예약 발송 상세",
        "schedule-list", "예약 발송 상세 조회");
    return "admin/schedule-detail";
  }

  @GetMapping("/admin/user-groups")
  public String userGroupList(
      @RequestParam(required = false) String keyword,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      Model model
  ) {
    setPage(model, "Billing System Admin - 사용자 그룹 목록", "사용자 그룹 목록",
        "user-group-list", "그룹/요금제/사용자 관리");

    Page<UserGroupListResponse> groupPage =
        userGroupService.getUserGroups(
            keyword,
            PageRequest.of(page, size)
        );
    long total = groupPage.getTotalElements();
    long start = page * size + 1;
    long end = Math.min((page + 1L) * size, total);

    model.addAttribute("groups", groupPage.getContent());
    model.addAttribute("groupPage", groupPage);
    model.addAttribute("keyword", keyword);
    model.addAttribute("page", page);
    model.addAttribute("size", size);
    model.addAttribute("total", total);
    model.addAttribute("start", start);
    model.addAttribute("end", end);
    return "admin/user-group-list";
  }

  @GetMapping({"/admin/user-groups/new", "/admin/user-groups/{groupId}/edit"})
  public String userGroupCreate(Model model,
      @PathVariable(value = "groupId", required = false) Long groupId) {
    setPage(model, "Billing System Admin - 사용자 그룹 등록", "사용자 그룹 등록",
        "user-group-list", "그룹 등록 및 사용자 추가");
    if (groupId != null) {
      model.addAttribute("group", userGroupService.getUserGroupDetail(groupId));
    }
    model.addAttribute("uplusServices", uplusServiceService.getServiceOptions());
    return "admin/user-group-form";
  }

  @GetMapping("/admin/user-groups/{groupId}")
  public String userGroupDetail(
      @PathVariable("groupId") Long groupId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "100") int size,
      Model model
  ) {
    setPage(
        model,
        "Billing System Admin - 사용자 그룹 상세",
        "사용자 그룹 상세",
        "user-group-list",
        "그룹 구성 상세 확인"
    );

    UserGroupDetailResponse group =
        userGroupService.getUserGroupDetail(groupId);
    Page<UserGroupUserResponse> users =
        userGroupService.getUsersByGroup(
            groupId,
            PageRequest.of(page, size)
        );

    model.addAttribute("group", group);
    model.addAttribute("users", users.getContent());
    model.addAttribute("userPage", users);

    model.addAttribute("page", page);
    model.addAttribute("size", size);

    return "admin/user-group-detail";
  }

  private void setPage(Model model, String title, String pageTitle, String activeKey,
      String pageStatus) {
    model.addAttribute("title", title);
    model.addAttribute("pageTitle", pageTitle);
    model.addAttribute("activeKey", activeKey);
    model.addAttribute("pageStatus", pageStatus);
  }

  private List<CodeResponse> getCodesByGroupCode(
      List<CodeGroupResponse> groups,
      String groupCode
  ) {
    return groups.stream()
        .filter(group -> Objects.equals(group.getCode(), groupCode))
        .findFirst()
        .map(CodeGroupResponse::getCodes)
        .orElse(List.of());
  }

  private Long getCodeIdByCode(List<CodeResponse> codes, String code) {
    if (code == null || code.isBlank()) {
      return null;
    }

    return codes.stream()
        .filter(c -> Objects.equals(c.getCode(), code))
        .map(CodeResponse::getId)
        .findFirst()
        .orElse(null);
  }
}
