package com.example.adminweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.adminweb.domain.message.MessageReservation;

public interface MessageReservationRepository extends JpaRepository<MessageReservation, Long> {
}
