-- liquibase formatted sql

-- changeset Konstantin:insert_products
INSERT INTO Products (Name, Description, Price,  CategoryId, ImageURL, DiscountPrice,  CreatedAt, UpdatedAt)
VALUES ('Organic Compost', 'Natural compost for enriching soil', 12.99, 1, 'images/organic_compost.jpg', 10.99, '2025-01-01', '2025-01-01'),
       ('Nitrogen Fertilizer', 'Boosts plant growth with high nitrogen content', 18.50, 1, 'images/nitrogen_fertilizer.jpg', 16.50, '2025-01-01', '2025-01-01'),
       ('Potassium Fertilizer', 'Improves root development and drought resistance', 14.75, 1, 'images/potassium_fertilizer.jpg', 12.75, '2025-01-01', '2025-01-01'),
       ('Liquid Organic Fertilizer', 'Easy-to-apply liquid fertilizer for vegetables', 20.99, 1, 'images/liquid_organic_fertilizer.jpg', 18.99, '2025-01-01', '2025-01-01'),

       -- Protective products and septic tanks (CategoryId = 2)
       ('Insect Repellent Spray', 'Protects plants from harmful insects', 9.99, 2, 'images/insect_repellent.jpg', 8.49, '2025-01-01', '2025-01-01'),
       ('Fungicide Powder', 'Prevents fungal infections in plants', 14.99, 2, 'images/fungicide_powder.jpg', 12.99, '2025-01-01', '2025-01-01'),
       ('Garden Slug Killer', 'Keeps slugs and snails away from plants', 11.25, 2, 'images/slug_killer.jpg', 9.99, '2025-01-01', '2025-01-01'),
       ('Septic Tank Bacteria', 'Enhances waste breakdown in septic systems', 25.00, 2, 'images/septic_bacteria.jpg', 22.00, '2025-01-01', '2025-01-01'),

       -- Planting material (CategoryId = 3)
       ('Rose Bush Seeds', 'Grow beautiful roses in your garden', 6.99, 3, 'images/rose_bush_seeds.jpg', 5.99, '2025-01-01', '2025-01-01'),
       ('Tomato Seedlings', 'Healthy tomato plants ready for transplanting', 4.50, 3, 'images/tomato_seedlings.jpg', 3.99, '2025-01-01', '2025-01-01'),
       ('Blueberry Bush', 'Produces delicious blueberries', 15.99, 3, 'images/blueberry_bush.jpg', 13.99, '2025-01-01', '2025-01-01'),
       ('Herb Garden Kit', 'Basil, parsley, and thyme seeds included', 9.75, 3, 'images/herb_garden_kit.jpg', 8.50, '2025-01-01', '2025-01-01'),

       -- Tools and equipment (CategoryId = 4)
       ('Garden Rake', 'Durable metal rake for soil leveling', 14.50, 4, 'images/garden_rake.jpg', 12.99, '2025-01-01', '2025-01-01'),
       ('Pruning Shears', 'Sharp pruning shears for trimming plants', 19.99, 4, 'images/pruning_shears.jpg', 17.49, '2025-01-01', '2025-01-01'),
       ('Hose with Spray Nozzle', 'Flexible garden hose with adjustable nozzle', 29.99, 4, 'images/garden_hose.jpg', 26.99, '2025-01-01', '2025-01-01'),
       ('Electric Lawn Mower', 'Cordless lawn mower with rechargeable battery', 199.99, 4, 'images/electric_lawn_mower.jpg', 179.99, '2025-01-01', '2025-01-01'),

       -- Pots and planters (CategoryId = 5)
       ('Ceramic Plant Pot', 'Stylish ceramic pot for indoor plants', 12.99, 5, 'images/ceramic_pot.jpg', 11.50, '2025-01-01', '2025-01-01'),
       ('Hanging Planter', 'Modern hanging planter for small spaces', 18.75, 5, 'images/hanging_planter.jpg', 16.99, '2025-01-01', '2025-01-01'),
       ('Large Plastic Planter', 'Durable plastic planter for outdoor use', 24.99, 5, 'images/plastic_planter.jpg', 21.99, '2025-01-01', '2025-01-01'),
       ('Self-Watering Pot', 'Maintains optimal soil moisture', 15.50, 5, 'images/self_watering_pot.jpg', 13.99, '2025-01-01', '2025-01-01');