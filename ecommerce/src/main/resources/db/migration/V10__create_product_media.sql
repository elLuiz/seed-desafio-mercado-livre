CREATE TABLE IF NOT EXISTS ecommerce.tb_product_media (
    filename VARCHAR(255) NOT NULL,
    url VARCHAR(500) NOT NULL,
    uploaded_at TIMESTAMP WITH TIME ZONE NOT NULL,
    fk_product_id BIGINT NOT NULL,

    constraint productMediaPK PRIMARY KEY(filename, fk_product_id),
    constraint productMediaFK FOREIGN KEY(fk_product_id)
        REFERENCES ecommerce.tb_product(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);