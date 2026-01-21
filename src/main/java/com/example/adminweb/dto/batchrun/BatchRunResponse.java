package com.example.adminweb.dto.batchrun;

import com.example.adminweb.dto.code.CodeResponse;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BatchRunResponse {

  private Long batchRunId;
  private String targetMonth;
  private String statusId;
  private String startedAt;
  private String endedAt;
  private Long durationMs;

  private Long totalCount;
  private Long successCount;
  private Long failCount;
  private String createdBy;

  private String result;
}