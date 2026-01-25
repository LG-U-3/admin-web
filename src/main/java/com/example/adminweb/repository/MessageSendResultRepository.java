package com.example.adminweb.repository;

import com.example.adminweb.domain.message.MessageSendResult;
import com.example.adminweb.repository.projection.MessagePublishProjection;
import com.example.adminweb.repository.projection.MessageRetryViewProjection;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MessageSendResultRepository extends JpaRepository<MessageSendResult, Long> {

  @Modifying
  @Query(value = """
      update message_send_results msr
      join message_templates mt
        on msr.template_id = mt.id
         set msr.status_id   = :waitingStatusId,
             msr.retry_count = msr.retry_count + 1,
             msr.processed_at = null
       where msr.id in (:ids)
         and msr.status_id = :failedStatusId
         and mt.purpose_type_id <> :billingPurposeTypeId
      """, nativeQuery = true)
  int retryToWaiting(
      @Param("ids") List<Long> ids,
      @Param("waitingStatusId") Long waitingStatusId,
      @Param("failedStatusId") Long failedStatusId,
      @Param("billingPurposeTypeId") Long billingPurposeTypeId
  );

  @Query(value = """
       select
           msr.id  as messageSendResultId,
           ch.code as channelCode,
           pur.code as purposeCode
       from message_send_results msr
       join message_templates mt
         on msr.template_id = mt.id
       join codes ch
         on mt.channel_type_id = ch.id
       join codes pur
         on mt.purpose_type_id = pur.id
      where msr.id in (:ids)
        and msr.status_id = :waitingStatusId
      """, nativeQuery = true)
  List<MessagePublishProjection> findPublishTargets(
      @Param("ids") List<Long> ids,
      @Param("waitingStatusId") Long waitingStatusId
  );

  @Query(
      value = """
           select
               msr.id          as messageSendResultId,
               u.id            as userId,
               ch.code         as channelCode,
               mt.code         as templateCode,
               st.code         as statusCode,
               msr.retry_count as retryCount
           from message_send_results msr
           join users u
             on msr.user_id = u.id
           join message_templates mt
             on msr.template_id = mt.id
           join codes ch
             on mt.channel_type_id = ch.id
           join codes st
             on msr.status_id = st.id
          where msr.status_id = :failedStatusId
            and mt.purpose_type_id <> :billingPurposeTypeId
          order by msr.requested_at desc
          """,
      countQuery = """
           select count(*)
           from message_send_results msr
           join message_templates mt
             on msr.template_id = mt.id
          where msr.status_id = :failedStatusId
            and mt.purpose_type_id <> :billingPurposeTypeId
          """,
      nativeQuery = true
  )
  Page<MessageRetryViewProjection> findRetryableMessages(
      @Param("failedStatusId") Long failedStatusId,
      @Param("billingPurposeTypeId") Long billingPurposeTypeId,
      Pageable pageable
  );
}
