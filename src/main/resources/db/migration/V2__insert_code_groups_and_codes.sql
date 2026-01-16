-- V2__insert_code_groups_and_codes.sql
-- Master(Code) data insert

SET NAMES utf8mb4;

-- =========================================================
-- 1) code_groups
-- =========================================================
INSERT IGNORE INTO code_groups (code, name) VALUES
('SERVICE_TYPE', '서비스 분류'),
('BATCH_STATUS', '배치 상태'),
('MESSAGE_SEND_STATUS', '메시지 전송 처리 상태'),
('MESSAGE_CHANNEL', '메시지 전송 채널'),
('MESSAGE_PURPOSE', '메시지 전송 목적'),
('RESERVATION_STATUS', '예약 발송 상태');

-- =========================================================
-- 2) codes
-- =========================================================

-- SERVICE_TYPE : PLAN / VAS / MCP
INSERT IGNORE INTO codes (code, name, group_id)
SELECT 'PLAN', '요금제', id FROM code_groups WHERE code='SERVICE_TYPE';
INSERT IGNORE INTO codes (code, name, group_id)
SELECT 'VAS', '부가서비스', id FROM code_groups WHERE code='SERVICE_TYPE';
INSERT IGNORE INTO codes (code, name, group_id)
SELECT 'MCP', '소액결제', id FROM code_groups WHERE code='SERVICE_TYPE';

-- BATCH_STATUS : STARTED / COMPLETED / FAILED
INSERT IGNORE INTO codes (code, name, group_id)
SELECT 'STARTED', '처리시작', id FROM code_groups WHERE code='BATCH_STATUS';
INSERT IGNORE INTO codes (code, name, group_id)
SELECT 'COMPLETED', '종료됨', id FROM code_groups WHERE code='BATCH_STATUS';
INSERT IGNORE INTO codes (code, name, group_id)
SELECT 'FAILED', '처리실패', id FROM code_groups WHERE code='BATCH_STATUS';

-- MESSAGE_SEND_STATUS : WAITING / PROCESSING / SUCCESS / FAILED / EXCEEDED
INSERT IGNORE INTO codes (code, name, group_id)
SELECT 'WAITING', '대기', id FROM code_groups WHERE code='MESSAGE_SEND_STATUS';
INSERT IGNORE INTO codes (code, name, group_id)
SELECT 'PROCESSING', '전송처리중', id FROM code_groups WHERE code='MESSAGE_SEND_STATUS';
INSERT IGNORE INTO codes (code, name, group_id)
SELECT 'SUCCESS', '성공', id FROM code_groups WHERE code='MESSAGE_SEND_STATUS';
INSERT IGNORE INTO codes (code, name, group_id)
SELECT 'FAILED', '실패', id FROM code_groups WHERE code='MESSAGE_SEND_STATUS';
INSERT IGNORE INTO codes (code, name, group_id)
SELECT 'EXCEEDED', '최대재시도초과', id FROM code_groups WHERE code='MESSAGE_SEND_STATUS';

-- MESSAGE_CHANNEL : EMAIL / SMS
INSERT IGNORE INTO codes (code, name, group_id)
SELECT 'EMAIL', '이메일', id FROM code_groups WHERE code='MESSAGE_CHANNEL';
INSERT IGNORE INTO codes (code, name, group_id)
SELECT 'SMS', 'SMS', id FROM code_groups WHERE code='MESSAGE_CHANNEL';

-- MESSAGE_PURPOSE : BILLING / NOTICE
INSERT IGNORE INTO codes (code, name, group_id)
SELECT 'BILLING', '정산서발송', id FROM code_groups WHERE code='MESSAGE_PURPOSE';
INSERT IGNORE INTO codes (code, name, group_id)
SELECT 'NOTICE', '알림', id FROM code_groups WHERE code='MESSAGE_PURPOSE';

-- RESERVATION_STATUS : WAITING / PROCESSING / SENT / CANCELED
INSERT IGNORE INTO codes (code, name, group_id)
SELECT 'WAITING', '발송대기', id FROM code_groups WHERE code='RESERVATION_STATUS';
INSERT IGNORE INTO codes (code, name, group_id)
SELECT 'PROCESSING', '발송처리중', id FROM code_groups WHERE code='RESERVATION_STATUS';
INSERT IGNORE INTO codes (code, name, group_id)
SELECT 'SENT', '발송됨', id FROM code_groups WHERE code='RESERVATION_STATUS';
INSERT IGNORE INTO codes (code, name, group_id)
SELECT 'CANCELED', '발송취소', id FROM code_groups WHERE code='RESERVATION_STATUS';
