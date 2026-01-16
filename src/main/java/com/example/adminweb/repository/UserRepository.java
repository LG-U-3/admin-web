package com.example.adminweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.adminweb.domain.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
