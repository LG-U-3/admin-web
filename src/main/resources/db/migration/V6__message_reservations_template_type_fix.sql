-- V6__message_reservations_template_type_fix.sql
SET NAMES utf8mb4;

UPDATE message_reservations mr
JOIN code_groups cg ON cg.code = 'MESSAGE_CHANNEL'
JOIN codes c ON c.group_id = cg.id
           AND c.code = mr.template_type_id
SET mr.template_type_id = c.id
WHERE mr.template_type_id IN ('SMS', 'EMAIL');

ALTER TABLE message_reservations
  MODIFY COLUMN template_type_id BIGINT NOT NULL;

ALTER TABLE message_reservations
  ADD CONSTRAINT fk_message_reservations_template_type
    FOREIGN KEY (template_type_id) REFERENCES codes(id);

CREATE INDEX idx_message_reservations_template_type
  ON message_reservations(template_type_id);
