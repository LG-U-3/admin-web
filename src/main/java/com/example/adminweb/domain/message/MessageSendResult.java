package com.example.adminweb.domain.message;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import com.example.adminweb.domain.code.Code;
import com.example.adminweb.domain.user.User;
import java.time.LocalDateTime;

@Entity
@Table(name = "message_send_results",
       indexes = {
               @Index(name = "idx_msr_status_requested", columnList = "status_id, requested_at"),
               @Index(name = "idx_msr_template", columnList = "template_id"),
               @Index(name = "idx_msr_user", columnList = "user_id"),
               @Index(name = "idx_msr_reserved", columnList = "reserved_send_id"),
               @Index(name = "idx_msr_channel", columnList = "channel_id")
       })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageSendResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reserved_send_id", nullable = false)
    private MessageReservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false)
    private Code channel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private MessageTemplate template;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private Code status;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime requestedAt;

    private LocalDateTime processedAt;

    @Column(nullable = false)
    private int retryCount;

    @Builder
    private MessageSendResult(MessageReservation reservation, User user,
                              Code channel, MessageTemplate template,
                              Code status, int retryCount) {
        this.reservation = reservation;
        this.user = user;
        this.channel = channel;
        this.template = template;
        this.status = status;
        this.retryCount = retryCount;
    }
}
