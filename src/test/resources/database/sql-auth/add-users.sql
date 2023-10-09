INSERT INTO users (id, email, first_name, last_name, password, is_delete)
values (1, 'user3@example.com', 'John', 'Sonny', '1234589kjhf', 0);

INSERT INTO users_roles (user_id, role_id) values (1, 1);