-- liquibase formatted sql

-- changeset Konstantin:insert_orders
INSERT INTO Orders(UserId, CreatedAt, DeliveryAddress, ContactPhone, DeliveryMethod, Status, UpdatedAt)
VALUES (1, '2025-01-01', '123 Main St', '555-1234', 'SELF_DELIVERY', 'CREATED', '2025-01-01'),
       (2, '2025-01-01', '456 Oak St', '555-5678', 'SELF_DELIVERY', 'CANCELED', '2025-01-01'),
       (3, '2025-01-01', '789 Pine St', '555-9101', 'DEPARTMENT_DELIVERY', 'AWAITING_PAYMENT','2025-01-01'),
       (4, '2025-01-01', '321 Birch St', '555-1122', 'SELF_DELIVERY', 'PAID', '2025-01-01'),
       (5, '2025-01-01', '654 Cedar St', '555-3344', 'DEPARTMENT_DELIVERY', 'ON_THE_WAY','2025-01-01'),
       (6, '2025-01-01', '987 Elm St', '555-5566', 'DEPARTMENT_DELIVERY', 'DELIVERED', '2025-01-01'),
       (7, '2025-01-01', '135 Maple St', '555-7788', 'DEPARTMENT_DELIVERY', 'CREATED', '2025-01-01'),
       (8, '2025-01-01', '246 Spruce St', '555-9900', 'SELF_DELIVERY', 'AWAITING_PAYMENT', '2025-01-01'),
       (9, '2025-01-01', '357 Willow St', '555-2233', 'DEPARTMENT_DELIVERY', 'PAID', '2025-01-01'),
       (10, '2025-01-01', '468 Ash St', '555-4455', 'DEPARTMENT_DELIVERY', 'CREATED', '2025-01-01'),
       (11, '2025-01-01', '579 Beech St', '555-6677', 'DEPARTMENT_DELIVERY', 'ON_THE_WAY','2025-01-01'),
       (12, '2025-01-01', '680 Fir St', '555-8899', 'DEPARTMENT_DELIVERY', 'CANCELED', '2025-01-01'),
       (13, '2025-01-01', '791 Pine St', '555-1011', 'SELF_DELIVERY', 'AWAITING_PAYMENT', '2025-01-01'),
       (14, '2025-01-01', '902 Oak St', '555-1213', 'DEPARTMENT_DELIVERY', 'PAID', '2025-01-01'),
       (15, '2025-01-01', '902 Oak St', '555-1213', 'DEPARTMENT_DELIVERY', 'PAID', '2025-01-01'),
       (16, '2025-01-01', '123 Birch St', '555-1415', 'DEPARTMENT_DELIVERY', 'CREATED', '2025-01-01'),
       (17, '2025-01-01', '234 Cedar St', '555-1617', 'DEPARTMENT_DELIVERY', 'CANCELED', '2025-01-01'),
       (18, '2025-01-01', '345 Elm St', '555-1819', 'DEPARTMENT_DELIVERY', 'AWAITING_PAYMENT', '2025-01-01'),
       (19, '2025-01-01', '567 Spruce St', '555-2223', 'DEPARTMENT_DELIVERY', 'CREATED', '2025-01-01'),
       (20, '2025-01-01', '678 Willow St', '555-2425', 'DEPARTMENT_DELIVERY', 'AWAITING_PAYMENT', '2025-01-01');