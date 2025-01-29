-- liquibase formatted sql

-- changeset Konstantin:insert_favorites
insert into Favorites (ProductId, UserId)
values (1, 1),
       (2, 2),
       (3, 3),
       (3, 4),
       (1, 5),
       (2, 6),
       (1, 7),
       (1, 8),
       (3, 8),
       (1, 6),
       (3, 17),
       (3, 13),
       (3, 1),
       (4, 20),
       (4, 18),
       (5, 15),
       (5, 5),
       (5, 4),
       (2, 10);