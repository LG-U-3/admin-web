-- V1__init_schema.sql
-- Billing System V1 Schema (tables + constraints + indexes)
-- MySQL 8.0 / InnoDB / utf8mb4

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS codes;
DROP TABLE IF EXISTS code_groups;

CREATE TABLE code_groups (
  id   BIGINT NOT NULL AUTO_INCREMENT,
  code VARCHAR(50) NOT NULL,
  name VARCHAR(100) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_code_groups_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE codes (
  id       BIGINT NOT NULL AUTO_INCREMENT,
  code     VARCHAR(50) NOT NULL,
  name     VARCHAR(100) NOT NULL,
  group_id BIGINT NOT NULL,
  PRIMARY KEY (id),
  KEY idx_codes_group_id (group_id),
  UNIQUE KEY uk_codes_group_code (group_id, code),
  CONSTRAINT fk_codes_group
    FOREIGN KEY (group_id) REFERENCES code_groups(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS users;
CREATE TABLE users (
  id        BIGINT NOT NULL AUTO_INCREMENT,
  name      VARCHAR(15) NOT NULL,
  -- join_date DATETIME NOT NULL,                -- ✅ [CHANGED] (V4) join_date 컬럼 삭제
  phone     VARCHAR(255) NOT NULL,
  email     VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_users_phone (phone),
  UNIQUE KEY uk_users_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS uplus_services;
CREATE TABLE uplus_services (
  id          BIGINT NOT NULL AUTO_INCREMENT,
  name        VARCHAR(100) NOT NULL,
  code        VARCHAR(30) NOT NULL,
  type_id     BIGINT NOT NULL,  -- 코드성 데이터(서비스 분류) (SERVICE_TYPE)
  price       INT NULL DEFAULT 0,
  description VARCHAR(255) NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_uplus_services_code (code),
  KEY idx_uplus_services_type_id (type_id),
  CONSTRAINT fk_uplus_services_type
    FOREIGN KEY (type_id) REFERENCES codes(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS user_groups;
CREATE TABLE user_groups (
  id          BIGINT NOT NULL AUTO_INCREMENT,
  code        VARCHAR(50) NOT NULL,
  name        VARCHAR(100) NOT NULL,
  description VARCHAR(255) NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_user_groups_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS message_templates;
CREATE TABLE message_templates (
  id              BIGINT NOT NULL AUTO_INCREMENT,
  code            VARCHAR(50) NOT NULL,     -- UNIQUE
  name            VARCHAR(50) NOT NULL,
  channel_type_id BIGINT NOT NULL,          -- codes.id (MESSAGE_CHANNEL)
  purpose_type_id BIGINT NOT NULL,          -- codes.id (MESSAGE_PURPOSE)
  title           VARCHAR(150) NULL,        -- EMAIL만 사용 가능
  body            TEXT NOT NULL,
  variables_json  JSON NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_message_templates_code (code),
  KEY idx_templates_channel_purpose (channel_type_id, purpose_type_id),
  CONSTRAINT fk_templates_channel
    FOREIGN KEY (channel_type_id) REFERENCES codes(id),
  CONSTRAINT fk_templates_purpose
    FOREIGN KEY (purpose_type_id) REFERENCES codes(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS message_reservations;
CREATE TABLE message_reservations (
  id               BIGINT NOT NULL AUTO_INCREMENT,
  scheduled_at     DATETIME NOT NULL,
  status_id        VARCHAR(20) NOT NULL,      -- 정의서 그대로(VARCHAR). 예: WAITING/SENT/CANCELED
  channel_type_id  VARCHAR(20) NOT NULL,      -- 정의서 그대로(VARCHAR). 예: EMAIL/SMS
  template_id      BIGINT NULL,
  template_type_id VARCHAR(10) NULL,
  user_group_id    BIGINT NOT NULL,
  PRIMARY KEY (id),
  KEY idx_reservations_scheduled_at (scheduled_at),
  KEY idx_reservations_status (status_id),
  KEY idx_reservations_group (user_group_id),
  KEY idx_reservations_template (template_id),
  CONSTRAINT fk_reservations_template
    FOREIGN KEY (template_id) REFERENCES message_templates(id),
  CONSTRAINT fk_reservations_user_group
    FOREIGN KEY (user_group_id) REFERENCES user_groups(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS batch_runs;
CREATE TABLE batch_runs (
  batch_run_id  BIGINT NOT NULL AUTO_INCREMENT,
  target_month  CHAR(7) NOT NULL,           -- YYYY-MM
  status_id     BIGINT NOT NULL,            -- codes.id (BATCH_STATUS)
  started_at    TIMESTAMP NOT NULL,
  ended_at      TIMESTAMP NULL,
  duration_ms   BIGINT NULL,
  total_count   BIGINT NULL,
  success_count BIGINT NULL,
  fail_count    BIGINT NULL,
  fail_reason   VARCHAR(500) NULL,
  created_by    VARCHAR(30) NULL,           -- AUTO/ADMIN
  PRIMARY KEY (batch_run_id),
  KEY idx_batch_runs_status_month (status_id, target_month),
  KEY idx_batch_runs_target_month (target_month),
  CONSTRAINT fk_batch_runs_status
    FOREIGN KEY (status_id) REFERENCES codes(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS charged_histories;
CREATE TABLE charged_histories (
  id                      BIGINT NOT NULL AUTO_INCREMENT,
  user_id                 BIGINT NOT NULL,
  service_id              BIGINT NOT NULL,
  created_at              DATETIME NOT NULL,
  charged_price           BIGINT NOT NULL DEFAULT 0,
  contract_discount_price BIGINT NULL,
  bundled_discount_price  BIGINT NULL,
  premier_discount_price  BIGINT NULL,
  PRIMARY KEY (id),
  KEY idx_charged_histories_user (user_id),
  KEY idx_charged_histories_service (service_id),
  KEY idx_charged_histories_created_at (created_at),
  CONSTRAINT fk_charged_histories_user
    FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_charged_histories_service
    FOREIGN KEY (service_id) REFERENCES uplus_services(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS billing_settlements;
CREATE TABLE billing_settlements (
  id           BIGINT NOT NULL AUTO_INCREMENT,
  batch_run_id BIGINT NOT NULL,
  target_month CHAR(7) NOT NULL,
  user_id      BIGINT NOT NULL,
  detail_json  JSON NOT NULL,
  final_amount INT NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  UNIQUE KEY uk_settlements_user_month (user_id, target_month),
  KEY idx_settlements_target_month (target_month),
  KEY idx_settlements_batch_run (batch_run_id),
  CONSTRAINT fk_settlements_batch_run
    FOREIGN KEY (batch_run_id) REFERENCES batch_runs(batch_run_id),
  CONSTRAINT fk_settlements_user
    FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS user_user_groups;
CREATE TABLE user_user_groups (
  id       BIGINT NOT NULL AUTO_INCREMENT,
  group_id BIGINT NOT NULL,
  user_id  BIGINT NOT NULL,
  PRIMARY KEY (id),
  KEY idx_uug_group (group_id),
  KEY idx_uug_user (user_id),
  UNIQUE KEY uk_uug_group_user (group_id, user_id),
  CONSTRAINT fk_uug_group
    FOREIGN KEY (group_id) REFERENCES user_groups(id),
  CONSTRAINT fk_uug_user
    FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS message_send_results;
CREATE TABLE message_send_results (
  id               BIGINT NOT NULL AUTO_INCREMENT,
  reserved_send_id BIGINT NOT NULL,         
  user_id          BIGINT NOT NULL,          
  channel_id       BIGINT NOT NULL,          
  template_id      BIGINT NOT NULL,          
  status_id        BIGINT NOT NULL,          -- codes.id (MESSAGE_STATUS)
  requested_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  processed_at     TIMESTAMP NULL,
  retry_count      INT NOT NULL DEFAULT 0,
  PRIMARY KEY (id),

  KEY idx_msr_status_requested (status_id, requested_at),
  KEY idx_msr_channel (channel_id),         
  KEY idx_msr_template (template_id),
  KEY idx_msr_user (user_id),
  KEY idx_msr_reserved (reserved_send_id),

  CONSTRAINT fk_msr_reservation
    FOREIGN KEY (reserved_send_id) REFERENCES message_reservations(id),
  CONSTRAINT fk_msr_user
    FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_msr_template
    FOREIGN KEY (template_id) REFERENCES message_templates(id),
  CONSTRAINT fk_msr_channel                 
    FOREIGN KEY (channel_id) REFERENCES codes(id),
  CONSTRAINT fk_msr_status
    FOREIGN KEY (status_id) REFERENCES codes(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;
