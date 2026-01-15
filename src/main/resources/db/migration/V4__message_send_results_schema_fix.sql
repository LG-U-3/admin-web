SET NAMES utf8mb4;

-- 1) users: join_date 컬럼 삭제
ALTER TABLE users
  DROP COLUMN join_date;

-- 2) message_send_results: 컬럼 제약(NOT NULL) 강화
ALTER TABLE message_send_results
  MODIFY reserved_send_id BIGINT NOT NULL,
  MODIFY user_id BIGINT NOT NULL,
  MODIFY template_id BIGINT NOT NULL;

-- 3) channel_id: DROP 후 재생성 (요구사항: 사진처럼)
ALTER TABLE message_send_results
  DROP COLUMN channel_id;

ALTER TABLE message_send_results
  ADD COLUMN channel_id BIGINT NOT NULL;

-- 4) FK 생성 (codes.id 참조)
ALTER TABLE message_send_results
  ADD CONSTRAINT fk_msr_channel
  FOREIGN KEY (channel_id) REFERENCES codes(id);

-- 5) 인덱스 생성 (성능/조회용)
CREATE INDEX idx_msr_channel ON message_send_results(channel_id);