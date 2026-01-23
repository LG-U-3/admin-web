package com.example.adminweb.controller;

import com.example.adminweb.dto.user.UserGroupListResponse;
import com.example.adminweb.dto.user.UserGroupRequest;
import com.example.adminweb.dto.user.UserGroupUserResponse;
import com.example.adminweb.service.UserGroupService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
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

  @PostMapping
  public Map<String, Long> create(@RequestBody UserGroupRequest request) {
    Long groupId = userGroupService.createUserGroup(request);
    return Map.of("groupId", groupId);
  }

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
}