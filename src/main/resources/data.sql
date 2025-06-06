DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;

CREATE TABLE users (
  username VARCHAR(50) PRIMARY KEY,
  password VARCHAR(255) NOT NULL,
  enabled BOOLEAN NOT NULL
);

CREATE TABLE roles (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL,
  role VARCHAR(50) NOT NULL,
  FOREIGN KEY (username) REFERENCES users(username)
);

-- Inserta usuarios (contraseñas encriptadas con BCrypt)
INSERT INTO users (username, password, enabled) VALUES
('admin', '$2a$10$eg/Vuj7GUnzYA3zdByJ32Ov5zcV3OdAncAGdoz919EcrXP510UZMC', true), -- password: admin123
('user', '$2a$10$Dow1ojE9l4QoI9qbdwMQ1Om4S9zOSm0b37N1uS8SglAo7J5LEHke2', true); -- password: user123

-- Inserta roles
INSERT INTO roles (username, role) VALUES
('admin', 'ROLE_ADMIN'),
('admin', 'ROLE_USER'),
('user', 'ROLE_USER');
