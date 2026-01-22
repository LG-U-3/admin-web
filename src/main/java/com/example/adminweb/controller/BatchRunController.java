package com.example.adminweb.controller;

import com.example.adminweb.dto.batchrun.BatchRunResponse;
import com.example.adminweb.service.BatchRunService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

  @PostMapping("/retry")
  public String retryBatch() {
    String result = batchRunService.retryBatch();
    System.out.println("API 요청완료");

    return result;
    // 이미 실행중인 배치가 있다면 alert("이미 실행중입니다!")
  }

}