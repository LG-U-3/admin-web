package com.example.adminweb.domain.message;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import com.example.adminweb.domain.user.UserGroup;

@Entity
@Table(name = "message_reservations",
       indexes = {
               @Index(name = "idx_reservations_scheduled_at", columnList = "scheduled_at"),
               @Index(name = "idx_reservations_status", columnList = "status_id"),
               @Index(name = "idx_reservations_group", columnList = "user_group_id"),
               @Index(name = "idx_reservations_template", columnList = "template_id")
       })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime scheduledAt;

    @Column(nullable = false, length = 20)
    private String statusId;

    @Column(nullable = false, length = 20)
    private String channelTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private MessageTemplate template;

    @Column(nullable = false, length = 10)
    private String templateTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_group_id", nullable = false)
    private UserGroup userGroup;

    @Builder
    private MessageReservation(LocalDateTime scheduledAt, String statusId,
                               String channelTypeId, MessageTemplate template,
                               String templateTypeId, UserGroup userGroup) {
        this.scheduledAt = scheduledAt;
        this.statusId = statusId;
        this.channelTypeId = channelTypeId;
        this.template = template;
        this.templateTypeId = templateTypeId;
        this.userGroup = userGroup;
    }
}
