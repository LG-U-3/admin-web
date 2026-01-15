package com.example.adminweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.adminweb.domain.user.UserUserGroup;

public interface UserUserGroupRepository extends JpaRepository<UserUserGroup, Long> {
}
