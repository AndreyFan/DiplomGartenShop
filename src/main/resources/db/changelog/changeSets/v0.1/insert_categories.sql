-- liquibase formatted sql

-- changeset Konstantin:insert_categories
INSERT INTO Categories (Name)
VALUES ('Fertilizer'),
       ('Protective products and septic tanks'),
       ('Planting material'),
       ('Tools and equipment'),
       ('Pots and planters');