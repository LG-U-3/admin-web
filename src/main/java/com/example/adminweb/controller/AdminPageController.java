package com.example.adminweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AdminPageController {

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
  public String templates(Model model) {
    setPage(model, "Billing System Admin - 템플릿 목록", "템플릿 목록", "templates",
        "템플릿 관리");
    return "admin/templates";
  }

  @GetMapping("/admin/templates/new")
  public String templateCreate(Model model) {
    setPage(model, "Billing System Admin - 템플릿 등록", "템플릿 등록", "templates",
        "신규 템플릿 등록");
    return "admin/template-create";
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
  public String userGroupList(Model model) {
    setPage(model, "Billing System Admin - 사용자 그룹 목록", "사용자 그룹 목록",
        "user-group-list", "그룹/요금제/사용자 관리");
    return "admin/user-group-list";
  }

  @GetMapping({"/admin/user-groups/new", "/admin/user-groups/{groupId}/edit"})
  public String userGroupCreate(Model model,
      @PathVariable(value = "groupId", required = false) String groupId) {
    setPage(model, "Billing System Admin - 사용자 그룹 등록", "사용자 그룹 등록",
        "user-group-list", "그룹 등록 및 사용자 추가");
    return "admin/user-group-form";
  }

  @GetMapping("/admin/user-groups/{groupId}")
  public String userGroupDetail(Model model, @PathVariable("groupId") String groupId) {
    setPage(model, "Billing System Admin - 사용자 그룹 상세", "사용자 그룹 상세",
        "user-group-list", "그룹 구성 상세 확인");
    return "admin/user-group-detail";
  }

  private void setPage(Model model, String title, String pageTitle, String activeKey,
      String pageStatus) {
    model.addAttribute("title", title);
    model.addAttribute("pageTitle", pageTitle);
    model.addAttribute("activeKey", activeKey);
    model.addAttribute("pageStatus", pageStatus);
  }
}
