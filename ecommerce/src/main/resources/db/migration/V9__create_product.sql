CREATE SEQUENCE IF NOT EXISTS ecommerce.seq_product_id START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS ecommerce.tb_product (
    id bigint PRIMARY KEY DEFAULT nextval('ecommerce.seq_product_id'),
    name varchar(255) NOT NULL,
    price double precision NOT NULL CHECK (price >= 2.0),
    stock_quantity int NOT NULL CHECK(stock_quantity >= 0),
    description text NOT NULL,
    fk_category_id bigint NOT NULL,
    fk_user_id bigint NOT NULL,

    constraint fkCategory FOREIGN KEY(fk_category_id) REFERENCES ecommerce.tb_category(id),
    constraint fkUserId FOREIGN KEY(fk_user_id) REFERENCES ecommerce.tb_user(id)
);

CREATE TABLE IF NOT EXISTS ecommerce.tb_product_characteristic (
    property VARCHAR(100) NOT NULL,
    property_value VARCHAR(100) NOT NULL,
    fk_product_id bigint NOT NULL,

    constraint pkProductCharacteristic PRIMARY KEY(property, property_value, fk_product_id),
    constraint fkProduct FOREIGN KEY(fk_product_id) REFERENCES ecommerce.tb_product(id)
);