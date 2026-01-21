package com.example.adminweb.repository;

import com.example.adminweb.domain.user.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long>,
    JpaSpecificationExecutor<UserGroup> {

}
