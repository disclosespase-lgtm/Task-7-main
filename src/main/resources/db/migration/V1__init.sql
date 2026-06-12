INSERT INTO roles (role) VALUES ('ROLE_USER');
INSERT INTO roles (role) VALUES ('ROLE_ADMIN');

INSERT INTO users (first_name, last_name, age, username, password)
VALUES ('User', 'Userov', 25, 'user', '1');

INSERT INTO users (first_name, last_name, age, username, password)
VALUES ('Admin', 'Adminov', 30, 'admin', '1');

INSERT INTO user_roles (user_id, roles_id) VALUES (1, 1);
INSERT INTO user_roles (user_id, roles_id) VALUES (2, 2);
INSERT INTO user_roles (user_id, roles_id) VALUES (2, 1);