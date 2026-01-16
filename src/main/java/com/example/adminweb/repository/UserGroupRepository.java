package com.example.adminweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.adminweb.domain.user.UserGroup;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
}
