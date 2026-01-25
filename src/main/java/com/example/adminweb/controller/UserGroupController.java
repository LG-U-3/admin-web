package com.example.adminweb.controller;

import com.example.adminweb.dto.user.UserGroupListResponse;
import com.example.adminweb.dto.user.UserGroupRequest;
import com.example.adminweb.dto.user.UserGroupUserResponse;
import com.example.adminweb.service.UserGroupService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/user-groups")
public class UserGroupController {

  private final UserGroupService userGroupService;

  @GetMapping
  public Page<UserGroupListResponse> list(
      @RequestParam(required = false) String keyword,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size
  ) {
    Pageable pageable = PageRequest.of(
        page,
        size,
        Sort.by("id").descending()
    );
    return userGroupService.getUserGroups(keyword, pageable);
  }

  // 등록
  @PostMapping
  public Map<String, Long> create(@RequestBody UserGroupRequest request) {
    log.info("Creating user group: {}, userIds: {}", request.getName(), request.getUserIds());
    Long groupId = userGroupService.createUserGroup(request);
    return Map.of("groupId", groupId);
  }

  // 수정
  @PutMapping("/{groupId}")
  public void update(@PathVariable Long groupId, @RequestBody UserGroupRequest request) {
    log.info("Updating user group: {}, userIds: {}", groupId, request.getUserIds());
    userGroupService.updateUserGroup(groupId, request);
  }

  // 해당 groupId에 포함된 users
  @GetMapping("/{groupId}/users")
  public Page<UserGroupUserResponse> users(
      @PathVariable Long groupId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size
  ) {
    Pageable pageable = PageRequest.of(
        page,
        size,
        Sort.by("id").ascending()
    );
    return userGroupService.getUsersByGroup(groupId, pageable);
  }

  // 해당 groupId에 포함된 userIds
  @GetMapping("/{groupId}/user-ids")
  public List<Long> userIds(@PathVariable Long groupId) {
    return userGroupService.getUserIdsByGroup(groupId);
  }

}