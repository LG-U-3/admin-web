package com.example.adminweb.repository;

import com.example.adminweb.domain.user.UserUserGroup;
import java.util.List;
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

  List<UserUserGroup> findAllByUserGroupId(Long groupId);
}