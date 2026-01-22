package com.example.adminweb.repository;

import com.example.adminweb.domain.message.MessageReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageReservationRepository extends JpaRepository<MessageReservation, Long> {

}
