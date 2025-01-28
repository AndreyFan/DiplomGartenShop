-- liquibase formatted sql

-- changeset konstantin:insert_products
INSERT INTO Products (Name, Description, Price, Quantity, CategoryId, ImageURL, DiscountPrice,  CreatedAt, UpdatedAt)
VALUES
    ('Garden Shovel', 'Heavy duty garden shovel for digging', 15.99, 15,1, 'images/garden_shovel.jpg', 13.99, '2025-01-01', '2025-01-01'),
    ('Garden Rake', 'Durable garden rake for cleaning up leaves', 12.99, 100, 1, 'images/garden_rake.jpg', 10.99, '2025-01-01', '2025-01-01'),
    ('Watering Can', 'Large capacity watering can for plants', 9.99,28, 1, 'images/watering_can.jpg', 8.49, '2025-01-01', '2025-01-01'),
    ('Garden Hoe', 'Sturdy garden hoe for weeding', 11.99,35, 1, 'images/garden_hoe.jpg', 9.99, '2025-01-01', '2025-01-01'),
    ('Pruning Shears', 'High quality pruning shears for trimming', 14.99,225, 1, 'images/pruning_shears.jpg', 12.99, '2025-01-01', '2025-01-01'),
    ('Lawn Mower', 'Electric lawn mower for cutting grass', 199.99, 45,2, 'images/lawn_mower.jpg', 179.99, '2025-01-01', '2025-01-01'),
    ('Garden Hose', 'Flexible garden hose with spray nozzle', 25.99,54, 1, 'images/garden_hose.jpg', 22.99, '2025-01-01', '2025-01-01'),
    ('Plant Pots', 'Set of 3 ceramic plant pots', 19.99,76, 1, 'images/plant_pots.jpg', 17.99, '2025-01-01', '2025-01-01'),
    ('Outdoor Furniture Set', '4-piece outdoor furniture set', 299.99,20, 3, 'images/outdoor_furniture.jpg', 269.99, '2025-01-01', '2025-01-01'),
    ('Barbecue Grill', 'Gas barbecue grill with 4 burners', 399.99, 167,7, 'images/barbecue_grill.jpg', 349.99, '2025-01-01', '2025-01-01'),
    ('Garden Gloves', 'Protective garden gloves', 7.99,23, 1, 'images/garden_gloves.jpg', 6.99, '2025-01-01', '2025-01-01'),
    ('Garden Cart', 'Heavy duty garden cart', 89.99,32, 1, 'images/garden_cart.jpg', 79.99, '2025-01-01', '2025-01-01'),
    ('Fertilizer', 'Organic plant fertilizer', 15.99,15, 1, 'images/fertilizer.jpg', 13.99, '2025-01-01', '2025-01-01'),
    ('Garden Shed', 'Large garden shed for storage', 499.99,77, 2, 'images/garden_shed.jpg', 449.99, '2025-01-01', '2025-01-01'),
    ('Outdoor Lights', 'Solar-powered outdoor lights', 29.99, 90,3, 'images/outdoor_lights.jpg', 25.99, '2025-01-01', '2025-01-01'),
    ('Compost Bin', 'Large compost bin', 59.99, 98,1, 'images/compost_bin.jpg', 49.99, '2025-01-01', '2025-01-01'),
    ('Bird Feeder', 'Decorative bird feeder', 14.99, 101,1, 'images/bird_feeder.jpg', 12.99, '2025-01-01', '2025-01-01'),
    ('Garden Statue', 'Decorative garden statue', 34.99,7, 1, 'images/garden_statue.jpg', 29.99, '2025-01-01', '2025-01-01'),
    ('Outdoor Heater', 'Propane outdoor heater', 149.99,54, 4, 'images/outdoor_heater.jpg', 129.99, '2025-01-01', '2025-01-01'),
    ('Patio Umbrella', 'Large patio umbrella', 89.99, 40,3, 'images/patio_umbrella.jpg', 79.99, '2025-01-01', '2025-01-01');