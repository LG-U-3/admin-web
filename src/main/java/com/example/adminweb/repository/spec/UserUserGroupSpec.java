package com.example.adminweb.repository.spec;

import com.example.adminweb.domain.user.UserUserGroup;
import org.springframework.data.jpa.domain.Specification;

public class UserUserGroupSpec {

  public static Specification<UserUserGroup> groupId(Long groupId) {
    return (root, query, cb) ->
        cb.equal(root.get("userGroup").get("id"), groupId);
  }
}
