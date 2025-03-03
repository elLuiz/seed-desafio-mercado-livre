CREATE SEQUENCE IF NOT EXISTS ecommerce.seq_tb_product_review_id START WITH 1
    INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS ecommerce.tb_product_review (
    id BIGINT PRIMARY KEY DEFAULT nextval('ecommerce.seq_tb_product_review_id'),
    rating int NOT NULL CHECK(rating BETWEEN 1 AND 5),
    title VARCHAR(255) NOT NULL,
    description VARCHAR(500) NOT NULL,
    review_created_at timestamp with time zone NOT NULL,
    fk_user_id BIGINT NOT NULL,
    fk_product_id BIGINT NOT NULL,

    constraint fkUserId FOREIGN KEY(fk_user_id) REFERENCES ecommerce.tb_user(id),
    constraint fkProductId FOREIGN KEY(fk_product_id) REFERENCES ecommerce.tb_product(id),
    constraint ukProductUserReview UNIQUE(fk_user_id, fk_product_id)
);