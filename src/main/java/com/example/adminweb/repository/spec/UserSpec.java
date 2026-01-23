package com.example.adminweb.repository.spec;

import com.example.adminweb.domain.charge.ChargedHistory;
import com.example.adminweb.domain.user.User;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
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
//  public static Specification<User> hasServiceId(Long serviceId) {
//    return (root, query, cb) -> {
//      if (serviceId == null || serviceId <= 0) {
//        return null;
//      }
//      Subquery<Long> subquery = query.subquery(Long.class);
//      Root<ChargedHistory> subRoot = subquery.from(ChargedHistory.class);
//      subquery.select(subRoot.get("userId"));
//      subquery.where(cb.equal(subRoot.get("service").get("id"), serviceId));
//
//      return root.get("id").in(subquery);
//    };
//  }
}