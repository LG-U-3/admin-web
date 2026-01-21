SET NAMES utf8mb4;

CREATE INDEX idx_mr_status_scheduled
ON message_reservations(status_id, scheduled_at);
