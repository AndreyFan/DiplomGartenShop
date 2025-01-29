-- liquibase formatted sql

-- changeset Konstantin:insert_cartItems
INSERT INTO CartItems (CartId, ProductId, Quantity)
VALUES (1, 2, 7),
       (1, 3, 5),
       (2, 9, 4),
       (3, 12, 8),
       (4, 5, 1),
       (5, 10, 3),
       (6, 4, 2),
       (6, 1, 8),
       (7, 3, 1),
       (8, 6, 10),
       (9, 20, 9),
       (10, 8, 8),
       (11, 1, 1),
       (12, 7, 7),
       (13, 17, 8),
       (13, 19, 3),
       (14, 3, 10),
       (15, 1, 2),
       (16, 7, 8),
       (17, 5, 1);