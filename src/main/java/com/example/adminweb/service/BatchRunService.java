package com.example.adminweb.service;

import com.example.adminweb.dto.batchrun.BatchRunResponse;
import com.example.adminweb.dto.service.UplusServiceOptionResponse;
import com.example.adminweb.repository.BatchRunRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BatchRunService {

  private final BatchRunRepository batchRunRepository;

  public List<BatchRunResponse> listBatchRuns(){
    return batchRunRepository.findAll().stream()
        .map(batchRun -> BatchRunResponse.builder()
            .batchRunId(batchRun.getId())
            .targetMonth(batchRun.getTargetMonth())
            .statusId(batchRun.getStatus() != null ? batchRun.getStatus().getName() : null)
            .startedAt(batchRun.getStartedAt() != null ? batchRun.getStartedAt().toString() : null)
            .endedAt(batchRun.getEndedAt() != null ? batchRun.getEndedAt().toString() : null)
            .durationMs(batchRun.getDurationMs())
            .totalCount(batchRun.getTotalCount())
            .successCount(batchRun.getSuccessCount())
            .createdBy(batchRun.getCreatedBy())
            .build())
        .toList();
  }

}
