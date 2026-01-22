package com.example.adminweb.repository;

import com.example.adminweb.domain.message.MessageReservation;
import com.example.adminweb.dto.messagereservation.MessageReservationListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageReservationQueryRepository extends JpaRepository<MessageReservation, Long> {

  @Query("""
    select new com.example.adminweb.dto.messagereservation.MessageReservationListResponse(
        r.id,
        ug.name,
        t.name,
        r.scheduledAt,
        r.status.code
    )
    from MessageReservation r
    join r.userGroup ug
    join r.template t
    where (:status is null or r.status.code = :status)
      and (:templateName is null or t.name like concat('%', :templateName, '%'))
      and (:groupName is null or ug.name like concat('%', :groupName, '%'))
    order by r.scheduledAt desc
  """)
  Page<MessageReservationListResponse> findReservations(
      @Param("status") String status,
      @Param("templateName") String templateName,
      @Param("groupName") String groupName,
      Pageable pageable
  );
}