package com.example.adminweb.service;

import com.example.adminweb.dto.service.UplusServiceOptionResponse;
import com.example.adminweb.repository.UplusServiceRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UplusServiceService {

  private final UplusServiceRepository uplusServiceRepository;

  public List<UplusServiceOptionResponse> getServiceOptions() {
    return uplusServiceRepository.findAll().stream()
        .map(service -> UplusServiceOptionResponse.builder()
            .code(service.getCode())
            .name(service.getName())
            .build())
        .toList();
  }
}
