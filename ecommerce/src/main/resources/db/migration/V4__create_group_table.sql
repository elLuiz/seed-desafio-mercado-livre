CREATE SEQUENCE IF NOT EXISTS ecommerce.seq_group_id START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS ecommerce.tb_group (
    id bigint PRIMARY KEY DEFAULT nextval('ecommerce.seq_group_id'),
    group_name varchar(120) NOT NULL,
    group_acronym varchar(150) NOT NULL,
    created_at timestamp DEFAULT (current_timestamp AT TIME ZONE 'UTC')
);