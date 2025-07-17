-- Таблица AUTHORITY
CREATE TABLE authority
(
    id   BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- Таблица ROLE
CREATE TABLE role
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_name    VARCHAR(100) NOT NULL,
    authority_id BIGINT,
    FOREIGN KEY (authority_id) REFERENCES authority (id)
);

-- Таблица USERS
CREATE TABLE users
(
    id       BIGINT PRIMARY KEY AUTO_INCREMENT,
    name     VARCHAR(100),
    password VARCHAR(100),
    email    VARCHAR(100) NOT NULL UNIQUE,
    role_id  BIGINT,
    FOREIGN KEY (role_id) REFERENCES role (id)
);
