INSERT INTO users (id, email, first_name, last_name, password, is_delete)
values (3, 'user18@example.com', 'John', 'Doe', '1234', 0);

INSERT INTO shopping_carts (id, user_id) VALUES (3, 3);