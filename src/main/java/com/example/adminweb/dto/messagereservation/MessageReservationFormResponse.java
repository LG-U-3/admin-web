package com.example.adminweb.dto.messagereservation;

import com.example.adminweb.dto.message.MessageTemplateListResponse;
import com.example.adminweb.dto.user.UserGroupSimpleResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MessageReservationFormResponse {
  private List<UserGroupSimpleResponse> userGroups;
  private List<MessageTemplateListResponse> templates;
}
