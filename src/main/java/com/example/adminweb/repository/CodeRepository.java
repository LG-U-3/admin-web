package com.example.adminweb.repository;

import com.example.adminweb.domain.code.Code;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeRepository extends JpaRepository<Code, Long> {
  
  List<Code> findAllByCodeGroupId(Long codeGroupId);

  List<Code> findAllByCodeGroupIdIn(List<Long> groupIds);
}
