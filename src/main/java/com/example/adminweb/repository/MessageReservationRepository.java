package com.example.adminweb.repository;

import com.example.adminweb.domain.message.MessageReservation;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageReservationRepository extends JpaRepository<MessageReservation, Long> {

  @Query("SELECT m FROM MessageReservation m JOIN FETCH m.status WHERE m.id = :id")
  Optional<MessageReservation> findByIdWithStatus(@Param("id") Long id);

  @Query("SELECT m FROM MessageReservation m " +
      "JOIN FETCH m.userGroup " +
      "JOIN FETCH m.template " +
      "WHERE m.id = :id")
  Optional<MessageReservation> findByIdWithRelations(@Param("id") Long id);

  @Modifying
  @Query("""
        update MessageReservation r
           set r.status.id = :statusId
         where r.id = :id
      """)
  void updateStatus(
      @Param("id") Long id,
      @Param("statusId") Long statusId
  );
}
