CREATE SEQUENCE IF NOT EXISTS ecommerce.seq_tb_category_id START WITH 1
    CACHE 1000
    INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS ecommerce.tb_category (
    id bigint PRIMARY KEY DEFAULT nextval('ecommerce.seq_tb_category_id'),
    category_name varchar(255) NOT NULL,
    created_at timestamp with time zone NOT NULL DEFAULT (CURRENT_TIMESTAMP AT TIME ZONE 'UTC'),
    status varchar(20) DEFAULT 'ACTIVE',
    fk_category_id bigint,

    constraint fkCategory FOREIGN KEY(fk_category_id) REFERENCES ecommerce.tb_category(id),
    constraint ukCategoryName UNIQUE(category_name)
);