CREATE TABLE user
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    enabled  BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE role
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE user_role
(
    user_id BIGINT NOT NULL,s
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES user (id),
    FOREIGN KEY (role_id) REFERENCES role (id)
);

INSERT INTO user(username, password, enabled)
VALUES ('admin', '$2a$10$EblZqNptyYjMgKPMQpYvUuHkG0kZFF7T4zF/heLx4rK9K1rY3LyVO', 1); -- 明文 123456

INSERT INTO role(name) VALUES ('ROLE_USER'), ('ROLE_ADMIN');
INSERT INTO user_role(user_id, role_id)
SELECT u.id, r.id
FROM user u, role r
WHERE u.username='admin' AND r.name='ROLE_ADMIN';

