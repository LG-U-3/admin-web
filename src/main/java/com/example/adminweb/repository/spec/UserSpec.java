package com.example.adminweb.repository.spec;

import com.example.adminweb.domain.user.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpec {

  public static Specification<User> nameLike(String name) {
    return (root, query, cb) -> {
      if (name == null || name.isBlank()) {
        return null;
      }
      return cb.like(
          root.get("name"),
          "%" + name + "%"
      );
    };
  }
}