package com.example.adminweb.repository;

import com.example.adminweb.domain.user.UserUserGroup;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserUserGroupRepository extends JpaRepository<UserUserGroup, Long>,
    JpaSpecificationExecutor<UserUserGroup> {

  @Query("""
          select uug.user.id
          from UserUserGroup uug
          where uug.userGroup.id = :groupId
      """)
  List<Long> findUserIdsByGroupId(@Param("groupId") Long groupId);

  long countByUserGroupId(Long groupId);
}
