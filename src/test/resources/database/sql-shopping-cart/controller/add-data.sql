INSERT INTO users (id, email, first_name, last_name, password, is_delete)
values (1, 'user3@example.com', 'John', 'Sonny', '1234', 0);

INSERT INTO shopping_carts (id, user_id) VALUES (1, 1);

INSERT INTO cart_items (id, book_id, quantity, is_delete, shopping_cart_id) VALUES (1, 1, 10, 0, 1);
INSERT INTO cart_items (id, book_id, quantity, is_delete, shopping_cart_id) VALUES (2, 2, 10, 0, 1);
INSERT INTO cart_items (id, book_id, quantity, is_delete, shopping_cart_id) VALUES (3, 4, 10, 0, 1);