SET NAMES utf8mb4;

SET @CG_MESSAGE_CHANNEL := (SELECT id FROM code_groups WHERE code='MESSAGE_CHANNEL');
SET @CG_MESSAGE_PURPOSE := (SELECT id FROM code_groups WHERE code='MESSAGE_PURPOSE');

SET @CODE_CHANNEL_EMAIL := (SELECT id FROM codes WHERE group_id=@CG_MESSAGE_CHANNEL AND code='EMAIL');
SET @CODE_CHANNEL_SMS   := (SELECT id FROM codes WHERE group_id=@CG_MESSAGE_CHANNEL AND code='SMS');
SET @CODE_PURPOSE_BILLING := (SELECT id FROM codes WHERE group_id=@CG_MESSAGE_PURPOSE AND code='BILLING');


-- =========================================================
-- 1) user_groups 기본 데이터
-- =========================================================
INSERT INTO user_groups (code, name, description)
VALUES ('USER_ALL', '전체유저', '전체유저')
ON DUPLICATE KEY UPDATE
  name = VALUES(name),
  description = VALUES(description);

-- =========================================================
-- 2) message_templates: DEFAULT_BILLING_SMS
-- =========================================================
INSERT INTO message_templates
  (code, name, channel_type_id, purpose_type_id, title, body, variables_json)
VALUES
  (
    'DEFAULT_BILLING_SMS',
    '기본정산서문자',
    @CODE_CHANNEL_SMS,
    @CODE_PURPOSE_BILLING,
    NULL,
    CONCAT(
      '{userName} 님, {targetMonth}의 요금은 {totalPrice} 원입니다.\n',
      '--------------------\n',
      '{serviceName} | {chargedPrice} 원\n',
      '"결합할인" | {bundledDiscountPrice}\n',
      '"프리미어할인" | {premierDiscountPrice}\n',
      '"약정할인" | {contractDiscountPrice}\n',
      '--------------------\n',
      '총 할인금액 {totalDiscount} 원,\n',
      '총 청구액 {totalPrice} 원입니다.'
    ),
    JSON_OBJECT(
      'targetMonth', '청구월',
      'userName', '유저이름',
      'serviceType', '서비스분류',
      'serviceName', '서비스명',
      'totalPrice', '총액',
      'createdAt', '청구일자',
      'bundledDiscountPrice', '결합할인금액',
      'premierDiscountPrice', '프리미어할인금액',
      'contractDiscountPrice', '약정할인금액',
      'totalDiscount', '총할인금액',
      'chargedPrice', '청구금액'
    )
  )
ON DUPLICATE KEY UPDATE
  name = VALUES(name),
  channel_type_id = VALUES(channel_type_id),
  purpose_type_id = VALUES(purpose_type_id),
  title = VALUES(title),
  body = VALUES(body),
  variables_json = VALUES(variables_json);

-- =========================================================
-- 3) message_templates: DEFAULT_BILLING_EMAIL
-- =========================================================
INSERT INTO message_templates
  (code, name, channel_type_id, purpose_type_id, title, body, variables_json)
VALUES
  (
    'DEFAULT_BILLING_EMAIL',
    '기본정산서이메일',
    @CODE_CHANNEL_EMAIL,
    @CODE_PURPOSE_BILLING,
    '{userName}님, {targetMonth}의 정산서가 도착하였습니다.',
    CONCAT(
      '주소 : {email}\n',
      '{userName}님, {targetMonth}의 정산서가 도착하였습니다.\n\n',
      '{userName} 님, {targetMonth}의 요금은 {totalPrice} 원입니다.\n',
      '--------------------\n',
      '{serviceName} | {chargedPrice} 원\n',
      '"결합할인" | {bundledDiscountPrice}\n',
      '"프리미어할인" | {premierDiscountPrice}\n',
      '"약정할인" | {contractDiscountPrice}\n',
      '--------------------\n',
      '총 할인금액 {totalDiscount} 원,\n',
      '총 청구액 {totalPrice} 원입니다.'
    ),
    JSON_OBJECT(
      'targetMonth', '청구월',
      'userName', '유저이름',
      'email', '이메일주소',
      'serviceType', '서비스분류',
      'serviceName', '서비스명',
      'totalPrice', '총액',
      'createdAt', '청구일자',
      'bundledDiscountPrice', '결합할인금액',
      'premierDiscountPrice', '프리미어할인금액',
      'contractDiscountPrice', '약정할인금액',
      'totalDiscount', '총할인금액',
      'chargedPrice', '청구금액'
    )
  )
ON DUPLICATE KEY UPDATE
  name = VALUES(name),
  channel_type_id = VALUES(channel_type_id),
  purpose_type_id = VALUES(purpose_type_id),
  title = VALUES(title),
  body = VALUES(body),
  variables_json = VALUES(variables_json);
