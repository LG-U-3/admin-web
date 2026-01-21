SET NAMES utf8mb4;

-- 1) 컬럼 추가 (처음엔 NULL 허용)
ALTER TABLE message_reservations
  ADD COLUMN target_month CHAR(7) NULL AFTER user_group_id;

UPDATE message_reservations
SET target_month = DATE_FORMAT(scheduled_at, '%Y-%m')
WHERE target_month IS NULL;

ALTER TABLE message_reservations
  MODIFY target_month CHAR(7) NOT NULL;
