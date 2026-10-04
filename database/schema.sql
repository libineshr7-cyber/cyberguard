-- CyberGuard database schema (MySQL 8+)
CREATE DATABASE IF NOT EXISTS cyberguard_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE cyberguard_db;

CREATE TABLE IF NOT EXISTS users (
  user_id       INT AUTO_INCREMENT PRIMARY KEY,
  name          VARCHAR(100) NOT NULL,
  email         VARCHAR(150) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  phone         VARCHAR(20),
  role          ENUM('CITIZEN','INVESTIGATOR','ADMIN') NOT NULL DEFAULT 'CITIZEN',
  created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS incidents (
  incident_id     INT AUTO_INCREMENT PRIMARY KEY,
  case_id         VARCHAR(40) NOT NULL UNIQUE,              -- CYB-YYYY-NNNNN
  user_id         INT NULL,                                 -- NULL = guest report
  reporter_name   VARCHAR(100) NOT NULL,
  reporter_email  VARCHAR(150) NOT NULL,
  reporter_phone  VARCHAR(20)  NOT NULL,
  incident_type   VARCHAR(40)  NOT NULL,
  description     TEXT NOT NULL,
  incident_date   DATE NOT NULL,
  suspicious_url  VARCHAR(500),
  financial_loss  DECIMAL(12,2) NOT NULL DEFAULT 0,
  additional_info TEXT,
  priority        ENUM('Low','Medium','High') NOT NULL DEFAULT 'Low',
  status          ENUM('SUBMITTED','UNDER_REVIEW','ASSIGNED','INVESTIGATION','RESOLVED','CLOSED') NOT NULL DEFAULT 'SUBMITTED',
  assigned_to     INT NULL,                                 -- investigator currently handling the case
  is_demo         TINYINT(1) NOT NULL DEFAULT 0,            -- 1 = sample record
  created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_inc_user FOREIGN KEY (user_id)     REFERENCES users(user_id) ON DELETE SET NULL,
  CONSTRAINT fk_inc_assg FOREIGN KEY (assigned_to) REFERENCES users(user_id) ON DELETE SET NULL,
  INDEX idx_inc_status (status), INDEX idx_inc_type (incident_type), INDEX idx_inc_user (user_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS evidence (
  evidence_id INT AUTO_INCREMENT PRIMARY KEY,
  incident_id INT NOT NULL,
  file_name   VARCHAR(120) NOT NULL,
  file_type   VARCHAR(60)  NOT NULL,
  file_path   VARCHAR(100) NOT NULL,
  uploaded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_ev_inc FOREIGN KEY (incident_id) REFERENCES incidents(incident_id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS investigations (
  investigation_id INT AUTO_INCREMENT PRIMARY KEY,
  incident_id      INT NOT NULL,
  investigator_id  INT NOT NULL,
  notes            TEXT NOT NULL,
  is_public        TINYINT(1) NOT NULL DEFAULT 1,           -- 1 = reporter may read it
  created_at       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_inv_inc  FOREIGN KEY (incident_id)     REFERENCES incidents(incident_id) ON DELETE CASCADE,
  CONSTRAINT fk_inv_user FOREIGN KEY (investigator_id) REFERENCES users(user_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS case_status_history (
  history_id  INT AUTO_INCREMENT PRIMARY KEY,
  incident_id INT NOT NULL,
  old_status  VARCHAR(20) NULL,
  new_status  VARCHAR(20) NOT NULL,
  updated_by  INT NULL,
  updated_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_hist_inc  FOREIGN KEY (incident_id) REFERENCES incidents(incident_id) ON DELETE CASCADE,
  CONSTRAINT fk_hist_user FOREIGN KEY (updated_by)  REFERENCES users(user_id) ON DELETE SET NULL
) ENGINE=InnoDB;
