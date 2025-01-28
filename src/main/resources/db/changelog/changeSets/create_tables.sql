-- liquibase formatted sql

-- changeset Konstantin:create_table_categories
CREATE TABLE Categories
(
    CategoryID INT AUTO_INCREMENT NOT NULL,
    Name       VARCHAR(255)           NULL,
    CONSTRAINT PK_CATEGORIES PRIMARY KEY (CategoryID)
);


-- changeset Konstantin:create_table_products
CREATE TABLE Products
(
    ProductID     INT AUTO_INCREMENT NOT NULL,
    Name          VARCHAR(255)           NULL,
    Description   VARCHAR(255)           NULL,
    Price         DECIMAL(10, 2)         NULL,
    Quantity      INT                    NULL,
    CategoryID    INT                    NULL,
    ImageURL      VARCHAR(255)           NULL,
    DiscountPrice DECIMAL(10, 2)         NULL,
    CreatedAt     TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UpdatedAt     TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT PK_PRODUCTS PRIMARY KEY (ProductID)
);


-- changeset Konstantin:create_table_users
CREATE TABLE Users
(
    UserID       INT AUTO_INCREMENT           NOT NULL,
    Name         VARCHAR(255)                     NULL,
    Email        VARCHAR(255)                     NULL,
    PhoneNumber  VARCHAR(255)                     NULL,
    PasswordHash VARCHAR(255)                     NULL,
    Role         ENUM ('CLIENT', 'ADMINISTRATOR') NULL,
    CONSTRAINT PK_USERS PRIMARY KEY (UserID)
);


-- changeset Konstantin:REFERENCES_PRODUCTS_CATEGORIES
ALTER TABLE Products ADD CONSTRAINT FK_PRODUCTS_CATEGORYID_REFERENCES FOREIGN KEY (CategoryID)
    REFERENCES Categories (CategoryID) ON UPDATE RESTRICT ON DELETE RESTRICT;