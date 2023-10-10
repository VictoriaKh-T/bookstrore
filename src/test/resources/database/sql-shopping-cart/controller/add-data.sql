INSERT INTO users (id, email, first_name, last_name, password, is_delete)
values (1, 'user3@example.com', 'John', 'Sonny', '1234', 0);

INSERT INTO shopping_carts (id, user_id) VALUES (1, 1);

/* books */
INSERT INTO books (id,title, isbn, price, author) VALUES (1,'To Kill a Mockingbird',
                                                             '978-0061120084', 200, 'Harper Lee');
INSERT INTO books (id,title, isbn, price, author) VALUES (2,'The Great Gatsby',
                                                          '978-0743273565', 120, 'F. Scott Fitzgerald');
INSERT INTO books (id,title, isbn, price, author) VALUES (3,'The Hobbit', '978-0547928227', 400, 'J.R.R. Tolkien');

/*cart items*/
INSERT INTO cart_items (id, book_id, quantity, is_delete, shopping_cart_id) VALUES (1, 1, 10, 0, 1);
INSERT INTO cart_items (id, book_id, quantity, is_delete, shopping_cart_id) VALUES (2, 2, 10, 0, 1);
INSERT INTO cart_items (id, book_id, quantity, is_delete, shopping_cart_id) VALUES (3, 3, 10, 0, 1);