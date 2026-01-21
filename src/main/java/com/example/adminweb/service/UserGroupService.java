package com.example.adminweb.service;

import com.example.adminweb.domain.user.UserGroup;
import com.example.adminweb.domain.user.UserUserGroup;
import com.example.adminweb.dto.user.UserGroupDetailResponse;
import com.example.adminweb.dto.user.UserGroupListResponse;
import com.example.adminweb.dto.user.UserGroupUserResponse;
import com.example.adminweb.repository.UserGroupRepository;
import com.example.adminweb.repository.UserUserGroupRepository;
import com.example.adminweb.repository.spec.UserGroupSpec;
import com.example.adminweb.repository.spec.UserUserGroupSpec;
import jakarta.persistence.criteria.JoinType;
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
}