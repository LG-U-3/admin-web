package com.example.adminweb.repository;

import com.example.adminweb.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<User, Long>,
    JpaSpecificationExecutor<User> {

}
