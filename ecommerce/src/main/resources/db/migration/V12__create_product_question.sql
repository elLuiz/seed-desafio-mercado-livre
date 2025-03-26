CREATE SEQUENCE IF NOT EXISTS ecommerce.seq_product_question_id START WITH 1
                                                             INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS ecommerce.tb_product_question (
    id bigint PRIMARY KEY DEFAULT nextval('ecommerce.seq_product_question_id'),
    question character varying(300) NOT NULL,
    fk_product_id bigint NOT NULL,
    fk_user_id bigint NOT NULL,
    created_at timestamp with time zone NOT NULL,

    constraint FKProduct FOREIGN KEY(fk_product_id) REFERENCES ecommerce.tb_product(id),
    constraint FKUser FOREIGN KEY(fk_user_id) REFERENCES ecommerce.tb_user(id)
);