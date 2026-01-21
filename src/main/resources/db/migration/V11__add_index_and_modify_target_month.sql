-- billing_settlements : final_amount 검색용 인덱스 추가
CREATE INDEX idx_final_amount
ON billing_settlements(final_amount);

-- message_reservations : target_month NULL 허용
ALTER TABLE message_reservations
MODIFY COLUMN target_month CHAR(7) NULL;