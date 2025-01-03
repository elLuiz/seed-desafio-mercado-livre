CREATE SEQUENCE IF NOT EXISTS ecommerce.seq_tb_user_id START WITH 1
    CACHE 1000
    INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS ecommerce.tb_user (
    id bigint PRIMARY KEY DEFAULT nextval('ecommerce.seq_tb_user_id'),
    full_name varchar(120) NOT NULL,
    login varchar(250) NOT NULL UNIQUE,
    password varchar(100) NOT NULL,
    expires_at timestamp with time zone NOT NULL
);