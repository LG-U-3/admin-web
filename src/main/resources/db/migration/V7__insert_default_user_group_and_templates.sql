-- V7__insert_default_user_group_and_templates.sql
-- Default seed data: USER_ALL group + default billing templates (SMS/EMAIL)
-- MySQL 8.0 / utf8mb4
-- Note: FK values(channel/purpose) are resolved via subqueries to codes(id)

SET NAMES utf8mb4;

-- =========================================================
-- 1) USER GROUP (전체유저)
-- =========================================================
INSERT INTO user_groups (code, name, description)
VALUES ('USER_ALL', '전체유저', '전체유저');

-- =========================================================
-- 2) MESSAGE TEMPLATES (DEFAULT BILLING: SMS / EMAIL)
--    - channel_type_id: codes.id (MESSAGE_CHANNEL: SMS/EMAIL)
--    - purpose_type_id: codes.id (MESSAGE_PURPOSE: BILLING)
--    - variables_json: 변수 "명세" (type/desc), 실제 값 X
-- =========================================================

-- 2-1) SMS Template
INSERT INTO message_templates
(code, name, channel_type_id, purpose_type_id, title, body, variables_json)
VALUES
(
  'DEFAULT_BILLING_SMS',
  '기본정산서문자',
  (SELECT c.id
     FROM codes c
     JOIN code_groups g ON g.id = c.group_id
    WHERE g.code = 'MESSAGE_CHANNEL' AND c.code = 'SMS'),
  (SELECT c.id
     FROM codes c
     JOIN code_groups g ON g.id = c.group_id
    WHERE g.code = 'MESSAGE_PURPOSE' AND c.code = 'BILLING'),
  NULL,
  '{userName}님, {targetMonth}의 요금은 {totalPrice}원입니다.\n'
  '--------------------\n'
  '{{#services}}'
  '- {serviceName} | {chargedPrice}원\n'
  '{{/services}}'
  '--------------------\n'
  '총 할인금액 {totalDiscount}원,\n'
  '총 청구액 {totalPrice}원입니다.',
  JSON_OBJECT(
    'targetMonth', JSON_OBJECT('type','string','desc','청구월(YYYY-MM)'),
    'userName', JSON_OBJECT('type','string','desc','유저 이름'),
    'services', JSON_OBJECT('type','array','desc','상품별 청구 내역(반복 출력 대상)'),
    'serviceName', JSON_OBJECT('type','string','desc','서비스명(services[] 내부)'),
    'chargedPrice', JSON_OBJECT('type','number','desc','서비스별 청구 금액(services[] 내부)'),
    'totalDiscount', JSON_OBJECT('type','number','desc','총 할인 금액'),
    'totalPrice', JSON_OBJECT('type','number','desc','총 청구 금액')
  )
);

-- 2-2) EMAIL Template
INSERT INTO message_templates
(code, name, channel_type_id, purpose_type_id, title, body, variables_json)
VALUES
(
  'DEFAULT_BILLING_EMAIL',
  '기본정산서이메일',
  (SELECT c.id
     FROM codes c
     JOIN code_groups g ON g.id = c.group_id
    WHERE g.code = 'MESSAGE_CHANNEL' AND c.code = 'EMAIL'),
  (SELECT c.id
     FROM codes c
     JOIN code_groups g ON g.id = c.group_id
    WHERE g.code = 'MESSAGE_PURPOSE' AND c.code = 'BILLING'),
  '{userName}님, {targetMonth}의 정산서가 도착하였습니다.',
  '주소: {email}\n'
  '{userName}님, {targetMonth}의 정산서가 도착하였습니다.\n\n'
  '{userName}님, {targetMonth}의 요금은 {totalPrice}원입니다.\n'
  '--------------------\n'
  '{{#services}}'
  '- {serviceName} | {chargedPrice}원\n'
  '{{/services}}'
  '--------------------\n'
  '총 할인금액 {totalDiscount}원,\n'
  '총 청구액 {totalPrice}원입니다.',
  JSON_OBJECT(
    'targetMonth', JSON_OBJECT('type','string','desc','청구월(YYYY-MM)'),
    'userName', JSON_OBJECT('type','string','desc','유저 이름'),
    'email', JSON_OBJECT('type','string','desc','이메일 주소'),
    'services', JSON_OBJECT('type','array','desc','상품별 청구 내역(반복 출력 대상)'),
    'serviceName', JSON_OBJECT('type','string','desc','서비스명(services[] 내부)'),
    'chargedPrice', JSON_OBJECT('type','number','desc','서비스별 청구 금액(services[] 내부)'),
    'totalDiscount', JSON_OBJECT('type','number','desc','총 할인 금액'),
    'totalPrice', JSON_OBJECT('type','number','desc','총 청구 금액')
  )
);
