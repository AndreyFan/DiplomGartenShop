-- liquibase formatted sql
drop table if exists Favorites;
drop table if exists OrderItems;
drop table if exists Orders;
drop table if exists CartItems;
drop table if exists Cart;
drop table if exists Users;
drop table if exists Products;
drop table if exists Categories;

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


-- changeset Konstantin:create_table_favorites
CREATE TABLE Favorites
(
    FavoriteID INT AUTO_INCREMENT NOT NULL,
    ProductID  INT                    NULL,
    UserID     INT                    NULL,
    CONSTRAINT PK_FAVORITES PRIMARY KEY (FavoriteID)
);


-- changeset Konstantin:create_table_orders
CREATE TABLE Orders
(
    OrderID         INT AUTO_INCREMENT                                                             NOT NULL,
    UserID          INT                                                                                NULL,
    CreatedAt       TIMESTAMP DEFAULT CURRENT_TIMESTAMP                                            NOT NULL,
    DeliveryAddress VARCHAR(255)                                                                       NULL,
    ContactPhone    VARCHAR(255)                                                                       NULL,
    DeliveryMethod  ENUM ('SELF_DELIVERY' ,'DEPARTMENT_DELIVERY')                                      NULL,
    Status          ENUM ('CREATED','CANCELED', 'AWAITING_PAYMENT', 'PAID', 'ON_THE_WAY', 'DELIVERED') NULL,
    UpdatedAt       TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT PK_ORDERS PRIMARY KEY (OrderID)
);


-- changeset Konstantin:create_table_orderItems
CREATE TABLE OrderItems
(
    OrderItemID     INT AUTO_INCREMENT NOT NULL,
    OrderID         INT                    NULL,
    ProductID       INT                    NULL,
    Quantity        INT                    NULL,
    PriceAtPurchase DECIMAL(10, 2)         NULL,
    CONSTRAINT PK_ORDERITEMS PRIMARY KEY (OrderItemID),
    FOREIGN KEY (ProductID) REFERENCES Products (ProductID)  --080225 ng
);


-- changeset Konstantin:create_table_cart
CREATE TABLE Cart
(
    CartID INT AUTO_INCREMENT NOT NULL,
    UserID INT                    NULL,
    CONSTRAINT PK_CART PRIMARY KEY (CartID),
    UNIQUE (UserID)
);


-- changeset Konstantin:create_table_cartItems
CREATE TABLE CartItems
(
    CartItemID INT AUTO_INCREMENT NOT NULL,
    CartID     INT                    NULL,
    ProductID  INT                    NULL,
    Quantity   INT                    NULL,
    CONSTRAINT PK_CARTITEMS PRIMARY KEY (CartItemID),
    FOREIGN KEY (ProductID) REFERENCES Products (ProductID)  --080225 ng
);




-- changeset Konstantin:create_foreign_key_products_categories
ALTER TABLE Products ADD CONSTRAINT foreign_key_products_categories FOREIGN KEY (CategoryID)
    REFERENCES Categories (CategoryID) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset Konstantin:create_foreign_key_favorites_products
ALTER TABLE Favorites
    ADD CONSTRAINT foreign_key_favorites_products FOREIGN KEY (ProductID) REFERENCES
        Products (ProductID) ON UPDATE CASCADE ON DELETE SET NULL;

-- changeset Konstantin:create_foreign_key_favorites_users
ALTER TABLE Favorites
    ADD CONSTRAINT foreign_key_favorites_users FOREIGN KEY (UserID) REFERENCES
        Users (UserID) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset Konstantin:create_foreign_key_orders_users
ALTER TABLE Orders
    ADD CONSTRAINT foreign_key_orders_users FOREIGN KEY (UserID) REFERENCES
        Users (UserID) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset Konstantin:create_foreign_key_orderItems_orders
ALTER TABLE OrderItems
    ADD CONSTRAINT foreign_key_orderItems_orders FOREIGN KEY (OrderID) REFERENCES
        Orders (OrderID) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset Konstantin:create_foreign_key_orderItems_products
ALTER TABLE OrderItems
    ADD CONSTRAINT foreign_key_orderItems_products FOREIGN KEY (ProductID) REFERENCES
        Products (ProductID) ON UPDATE CASCADE ON DELETE SET NULL;

-- changeset Konstantin:create_foreign_key_cart_users
ALTER TABLE Cart
    ADD CONSTRAINT foreign_key_cart_users FOREIGN KEY (UserID) REFERENCES
        Users (UserID) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset Konstantin:create_foreign_key_cartItems_cart
ALTER TABLE CartItems
    ADD CONSTRAINT foreign_key_cartItems_cart FOREIGN KEY (CartID) REFERENCES
        Cart (CartID) ON UPDATE RESTRICT ON DELETE RESTRICT;

-- changeset Konstantin:create_foreign_key_cartItems_products
ALTER TABLE CartItems
    ADD CONSTRAINT foreign_key_cartItems_products FOREIGN KEY (ProductID) REFERENCES
        Products (ProductID) ON UPDATE CASCADE ON DELETE SET NULL;