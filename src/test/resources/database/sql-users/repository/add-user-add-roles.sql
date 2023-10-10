INSERT INTO users (id, email, first_name, last_name, password, is_delete)
values (3, 'user3@example.com', 'John', 'Sonny', '1234', 0);

INSERT INTO users_roles (user_id, role_id) values (3, 1);
INSERT INTO users_roles (user_id, role_id) values (3, 2);