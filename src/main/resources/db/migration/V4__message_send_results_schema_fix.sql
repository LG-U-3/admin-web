
ALTER TABLE message_send_results
  MODIFY reserved_send_id BIGINT NOT NULL,
  MODIFY user_id BIGINT NOT NULL,
  MODIFY template_id BIGINT NOT NULL;