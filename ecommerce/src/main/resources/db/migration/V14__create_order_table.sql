CREATE SEQUENCE IF NOT EXISTS ecommerce.seq_tb_order_id START WITH 1 INCREMENT BY 1;

CREATE TABLE ecommerce.tb_order (
    id BIGINT PRIMARY KEY DEFAULT nextval('ecommerce.seq_tb_order_id'),
    fk_customer_id BIGINT NOT NULL,
    purchased_at TIMESTAMP WITH TIME ZONE NOT NULL,
    selected_gateway VARCHAR(20) NOT NULL,
    fk_product_id BIGINT NOT NULL,
    quantity int NOT NULL check(quantity > 0),
    price numeric(10, 2) NOT NULL,
    order_unique_id varchar(64) NOT NULL,

    constraint fkCustomer FOREIGN KEY(fk_customer_id)
        REFERENCES ecommerce.tb_user(id),
    constraint fkProductId FOREIGN KEY(fk_product_id)
        REFERENCES ecommerce.tb_product(id)
)