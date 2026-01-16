package com.example.adminweb.repository;

import com.example.adminweb.domain.user.UserUserGroup;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserUserGroupRepository extends JpaRepository<UserUserGroup, Long> {

  @Query("""
          select uug.userId
          from UserUserGroup uug
          where uug.userGroup.id = :groupId
      """)
  List<Long> findUserIdsByGroupId(@Param("groupId") Long groupId);
}
