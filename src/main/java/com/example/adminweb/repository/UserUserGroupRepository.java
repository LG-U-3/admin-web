package com.example.adminweb.repository;

import com.example.adminweb.domain.user.UserUserGroup;
import jakarta.annotation.Nullable;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserUserGroupRepository extends JpaRepository<UserUserGroup, Long>,
    JpaSpecificationExecutor<UserUserGroup> {

  long countByUserGroupId(Long groupId);

  @Modifying
  @Query("delete from UserUserGroup u where u.userGroup.id = :groupId")
  void deleteByUserGroupId(@Param("groupId") Long groupId);

  @Override
  @EntityGraph(attributePaths = "user")
  Page<UserUserGroup> findAll(@Nullable Specification<UserUserGroup> spec, Pageable pageable);

  List<UserUserGroup> findAllByUserGroupId(Long groupId);
}