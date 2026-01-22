package com.example.adminweb.repository;

import com.example.adminweb.domain.user.UserUserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserUserGroupRepository extends JpaRepository<UserUserGroup, Long>,
    JpaSpecificationExecutor<UserUserGroup> {

  long countByUserGroupId(Long groupId);
}
