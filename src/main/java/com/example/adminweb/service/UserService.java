package com.example.adminweb.service;

import com.example.adminweb.domain.user.User;
import com.example.adminweb.dto.user.UserSearchResponse;
import com.example.adminweb.repository.UserRepository;
import com.example.adminweb.repository.spec.UserSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository userRepository;

  public Page<UserSearchResponse> searchUsers(
      String name,
      Pageable pageable
  ) {
    Page<User> page = userRepository.findAll(
        UserSpec.nameLike(name),
        pageable
    );

    return page.map(user ->
        UserSearchResponse.builder()
            .userId(user.getId())
            .name(user.getName())
            .build()
    );
  }
}