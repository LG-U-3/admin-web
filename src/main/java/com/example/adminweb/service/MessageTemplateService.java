package com.example.adminweb.service;

import static com.example.adminweb.repository.spec.MessageTemplateSpec.channelType;
import static com.example.adminweb.repository.spec.MessageTemplateSpec.keyword;
import static com.example.adminweb.repository.spec.MessageTemplateSpec.purposeType;

import com.example.adminweb.domain.message.MessageTemplate;
import com.example.adminweb.dto.message.MessageTemplateCreateRequest;
import com.example.adminweb.dto.message.MessageTemplateListResponse;
import com.example.adminweb.repository.CodeRepository;
import com.example.adminweb.repository.MessageTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageTemplateService {

  private final MessageTemplateRepository messageTemplateRepository;
  private final CodeRepository codeRepository;

  @Transactional
  public void createTemplate(MessageTemplateCreateRequest request) {
    var channelType = codeRepository.findById(request.getChannelTypeId())
        .orElseThrow(() -> new IllegalArgumentException("채널 타입이 유효하지 않습니다."));
    var purposeType = codeRepository.findById(request.getPurposeTypeId())
        .orElseThrow(() -> new IllegalArgumentException("목적 타입이 유효하지 않습니다."));

    MessageTemplate template = MessageTemplate.builder()
        .code(request.getCode())
        .name(request.getName())
        .channelType(channelType)
        .purposeType(purposeType)
        .title(request.getTitle())
        .body(request.getBody())
        .variablesJson(request.getVariablesJson())
        .build();

    messageTemplateRepository.save(template);
  }

  public Page<MessageTemplateListResponse> getTemplates(
      Long channelTypeId,
      Long purposeTypeId,
      String keyword,
      Pageable pageable
  ) {
    return messageTemplateRepository.findAll(
        channelType(channelTypeId)
            .and(purposeType(purposeTypeId))
            .and(keyword(keyword)),
        pageable
    ).map(t -> MessageTemplateListResponse.builder()
        .id(t.getId())
        .code(t.getCode())
        .name(t.getName())
        .channelTypeId(t.getChannelType().getId())
        .channelTypeCode(t.getChannelType().getCode())
        .purposeTypeId(t.getPurposeType().getId())
        .purposeTypeCode(t.getPurposeType().getCode())
        .title(t.getTitle())
        .build()
    );
  }
}
