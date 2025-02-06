CREATE SEQUENCE IF NOT EXISTS ecommerce.seq_role_id START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS ecommerce.tb_role (
    id bigint PRIMARY KEY DEFAULT nextval('ecommerce.seq_role_id'),
    role_description varchar(500) NOT NULL,
    role_acronym varchar(100) NOT NULL,
    created_at timestamp DEFAULT (current_timestamp AT TIME ZONE 'UTC')
);