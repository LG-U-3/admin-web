package com.example.adminweb.service;

import com.example.adminweb.domain.user.User;
import com.example.adminweb.domain.user.UserGroup;
import com.example.adminweb.domain.user.UserUserGroup;
import com.example.adminweb.dto.user.UserGroupDetailResponse;
import com.example.adminweb.dto.user.UserGroupListResponse;
import com.example.adminweb.dto.user.UserGroupRequest;
import com.example.adminweb.dto.user.UserGroupUserResponse;
import com.example.adminweb.repository.UserGroupRepository;
import com.example.adminweb.repository.UserRepository;
import com.example.adminweb.repository.UserUserGroupRepository;
import com.example.adminweb.repository.spec.UserGroupSpec;
import com.example.adminweb.repository.spec.UserUserGroupSpec;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserGroupService {

  private final UserGroupRepository userGroupRepository;
  private final UserUserGroupRepository userUserGroupRepository;
  private final UserRepository userRepository;

  public Page<UserGroupListResponse> getUserGroups(
      String keyword,
      Pageable pageable
  ) {
    Page<UserGroup> page = userGroupRepository.findAll(
        UserGroupSpec.keyword(keyword),
        pageable
    );

    return page.map(group ->
        UserGroupListResponse.builder()
            .groupId(group.getId())
            .code(group.getCode())
            .name(group.getName())
            .userCount(
                userUserGroupRepository.countByUserGroupId(group.getId())
            )
            .build()
    );
  }

  public Page<UserGroupUserResponse> getUsersByGroup(
      Long groupId,
      Pageable pageable
  ) {
    Page<UserUserGroup> page =
        userUserGroupRepository.findAll(
            (root, query, cb) -> {
              root.fetch("user", JoinType.INNER);
              query.distinct(true);
              return UserUserGroupSpec.groupId(groupId)
                  .toPredicate(root, query, cb);
            },
            pageable
        );

    return page.map(uug ->
        UserGroupUserResponse.builder()
            .userId(uug.getUser().getId())
            .name(uug.getUser().getName())
            .build()
    );
  }

  public List<Long> getUserIdsByGroup(Long groupId) {
    return userUserGroupRepository.findAllByUserGroupId(groupId).stream()
        .map(uug -> uug.getUser().getId())
        .collect(Collectors.toList());
  }

  public UserGroupDetailResponse getUserGroupDetail(
      Long groupId
  ) {
    UserGroup group = userGroupRepository.findById(groupId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 그룹"));

    return UserGroupDetailResponse.builder()
        .groupId(group.getId())
        .code(group.getCode())
        .groupName(group.getName())
        .description(group.getDescription())
        .build();
  }

  @Transactional
  public Long createUserGroup(UserGroupRequest request) {
    UserGroup userGroup = UserGroup.builder()
        .code(request.getCode())
        .name(request.getName())
        .description(request.getDescription())
        .build();

    userGroupRepository.save(userGroup);

    if (request.getUserIds() != null && !request.getUserIds().isEmpty()) {
      List<User> users = userRepository.findAllById(request.getUserIds());
      for (User user : users) {
        UserUserGroup userUserGroup = UserUserGroup.builder()
            .userGroup(userGroup)
            .user(user)
            .build();
        userUserGroupRepository.save(userUserGroup);
      }
    }

    return userGroup.getId();
  }

  @Transactional
  public void updateUserGroup(Long groupId, UserGroupRequest request) {
    UserGroup userGroup = userGroupRepository.findById(groupId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 그룹"));

    userGroup.update(request.getCode(), request.getName(), request.getDescription());

    // 기존 매핑 삭제
    userUserGroupRepository.deleteByUserGroupId(groupId);

    // 새 매핑 추가
    if (request.getUserIds() != null && !request.getUserIds().isEmpty()) {
      List<User> users = userRepository.findAllById(request.getUserIds());
      for (User user : users) {
        UserUserGroup userUserGroup = UserUserGroup.builder()
            .userGroup(userGroup)
            .user(user)
            .build();
        userUserGroupRepository.save(userUserGroup);
      }
    }
  }

}