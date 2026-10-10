INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES (1, 'Sample Book 1', 'Author 1', '978-0132350884', 29.99, 'Description 1', 'cover1.jpg', false);
INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES (2, 'Sample Book 2', 'Author 2', '978-0134685991', 39.99, 'Description 2', 'cover2.jpg', false);

INSERT INTO books_categories (book_id, category_id) VALUES (1, 1);
INSERT INTO books_categories (book_id, category_id) VALUES (2, 1);