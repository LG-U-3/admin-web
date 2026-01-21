package com.example.adminweb.controller;

import com.example.adminweb.dto.user.UserSearchResponse;
import com.example.adminweb.service.UserService;
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
@RequestMapping("/api/admin/users")
public class UserController {

  private final UserService userService;

  @GetMapping
  public Page<UserSearchResponse> search(
      @RequestParam(required = false) String name,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size
  ) {
    Pageable pageable = PageRequest.of(
        page,
        size,
        Sort.by("id").ascending()
    );

    return userService.searchUsers(name, pageable);
  }
}