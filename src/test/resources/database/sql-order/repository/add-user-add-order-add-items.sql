INSERT INTO users (id, email, first_name, last_name, password, is_delete)
values (3, 'user3@example.com', 'John', 'Sonny', '1234', 0);

INSERT INTO orders(id, user_id, status, total, order_date, shipping_address)
values (1, 3, "NEW", 450, '2023-10-08', "adress exemple");

INSERT INTO order_items(id, book_id, quantity, price, order_id, is_delete)
values (1, 1, 10, 200, 1, 0);

INSERT INTO order_items(id, book_id, quantity, price, order_id, is_delete)
values (2, 2, 10, 100, 1, 0);

INSERT INTO order_items(id, book_id, quantity, price, order_id, is_delete)
values (3, 4, 1, 150, 1, 0);