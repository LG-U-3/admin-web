-- V5__message_reservations_code_fk.sql
SET NAMES utf8mb4;

ALTER TABLE message_reservations
  MODIFY COLUMN status_id BIGINT NOT NULL;

ALTER TABLE message_reservations
  MODIFY COLUMN channel_type_id BIGINT NOT NULL;

ALTER TABLE message_reservations
  ADD CONSTRAINT fk_message_reservations_status
    FOREIGN KEY (status_id) REFERENCES codes(id),
  ADD CONSTRAINT fk_message_reservations_channel_type
    FOREIGN KEY (channel_type_id) REFERENCES codes(id);

CREATE INDEX idx_message_reservations_status
  ON message_reservations(status_id);

CREATE INDEX idx_message_reservations_channel_type
  ON message_reservations(channel_type_id);
