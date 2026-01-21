package com.example.adminweb.controller;

import com.example.adminweb.dto.batchrun.BatchRunResponse;
import com.example.adminweb.service.BatchRunService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/batches")
@RequiredArgsConstructor
public class BatchRunController {

  private final BatchRunService batchRunService;

  @GetMapping
  public List<BatchRunResponse> listBatchRuns() {
    return batchRunService.listBatchRuns();
  }

}