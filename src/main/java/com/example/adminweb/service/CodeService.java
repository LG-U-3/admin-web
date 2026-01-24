package com.example.adminweb.service;

import com.example.adminweb.domain.code.Code;
import com.example.adminweb.domain.code.CodeGroup;
import com.example.adminweb.dto.code.CodeGroupResponse;
import com.example.adminweb.dto.code.CodeResponse;
import com.example.adminweb.repository.CodeGroupRepository;
import com.example.adminweb.repository.CodeRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CodeService {

  private final CodeGroupRepository codeGroupRepository;
  private final CodeRepository codeRepository;

  public List<CodeGroupResponse> getCodeGroupsWithCodes() {

    List<CodeGroup> groups = codeGroupRepository.findAll();
    if (groups.isEmpty()) {
      return List.of();
    }

    List<Long> groupIds = groups.stream()
        .map(CodeGroup::getId)
        .toList();

    List<Code> codes = codeRepository.findAllByCodeGroupIdIn(groupIds);

    Map<Long, List<CodeResponse>> codesByGroupId =
        codes.stream()
            .collect(Collectors.groupingBy(
                c -> c.getCodeGroup().getId(),
                Collectors.mapping(
                    c -> CodeResponse.builder()
                        .id(c.getId())
                        .code(c.getCode())
                        .name(c.getName())
                        .build(),
                    Collectors.toList()
                )
            ));

    return groups.stream()
        .map(group -> CodeGroupResponse.builder()
            .id(group.getId())
            .code(group.getCode())
            .name(group.getName())
            .codes(
                codesByGroupId.getOrDefault(group.getId(), List.of())
            )
            .build()
        )
        .toList();
  }

  public List<Code> getChannelTypes() {
    return codeRepository.findAllByCodeGroupCode("MESSAGE_CHANNEL");
  }

  public List<Code> getPurposeTypes() {
    return codeRepository.findAllByCodeGroupCode("MESSAGE_PURPOSE");
  }
}