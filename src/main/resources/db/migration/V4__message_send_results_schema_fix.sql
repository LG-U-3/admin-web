SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 1) users: join_date 컬럼 삭제
ALTER TABLE users
  DROP COLUMN join_date;

-- 2) message_send_results: 컬럼 제약(NOT NULL) 강화
ALTER TABLE message_send_results
  MODIFY reserved_send_id BIGINT NOT NULL,
  MODIFY user_id BIGINT NOT NULL,
  MODIFY template_id BIGINT NOT NULL;

ALTER TABLE message_send_results
  MODIFY channel_id BIGINT NOT NULL;

CREATE INDEX idx_msr_channel ON message_send_results(channel_id);


ALTER TABLE message_send_results
  ADD CONSTRAINT fk_msr_channel
  FOREIGN KEY (channel_id) REFERENCES codes(id);


SET FOREIGN_KEY_CHECKS = 1;