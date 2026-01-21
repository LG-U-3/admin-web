package com.example.adminweb.repository.spec;

import com.example.adminweb.domain.user.UserGroup;
import org.springframework.data.jpa.domain.Specification;

public class UserGroupSpec {

  public static Specification<UserGroup> keyword(String keyword) {
    return (root, query, cb) -> {
      if (keyword == null || keyword.isBlank()) {
        return null;
      }
      return cb.or(
          cb.like(root.get("code"), "%" + keyword + "%"),
          cb.like(root.get("name"), "%" + keyword + "%")
      );
    };
  }
}
