SET NAMES utf8mb4;

DELETE mr
FROM message_reservations mr
JOIN message_reservations dup
  ON mr.target_month = dup.target_month
 AND mr.template_id = dup.template_id
 AND mr.user_group_id = dup.user_group_id
 AND mr.id < dup.id;

-- 2) UNIQUE 추가
ALTER TABLE message_reservations
ADD CONSTRAINT uk_month_template_group
UNIQUE (target_month, template_id, user_group_id);