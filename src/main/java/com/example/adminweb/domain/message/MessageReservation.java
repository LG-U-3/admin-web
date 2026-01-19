@Entity
@Table(name = "message_reservations")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "scheduled_at", nullable = false)
    private LocalDateTime scheduledAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private Code status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_type_id", nullable = false)
    private Code channelType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private MessageTemplate template;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_group_id", nullable = false)
    private com.example.adminweb.domain.user.UserGroup userGroup;

    @Builder
    private MessageReservation(
            LocalDateTime scheduledAt,
            Code status,
            Code channelType,
            MessageTemplate template,
            com.example.adminweb.domain.user.UserGroup userGroup
    ) {
        this.scheduledAt = scheduledAt;
        this.status = status;
        this.channelType = channelType;
        this.template = template;
        this.userGroup = userGroup;
    }
}
