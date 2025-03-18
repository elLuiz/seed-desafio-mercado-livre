CREATE SEQUENCE IF NOT EXISTS ecommerce.seq_tb_tb_outbox_table_id START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS ecommerce.tb_outbox_table (
    id bigint PRIMARY KEY DEFAULT nextval('ecommerce.seq_tb_tb_outbox_table_id'),
    created_at timestamp with time zone DEFAULT current_timestamp,
    topic varchar(255) NOT NULL,
    aggregate_id varchar NOT NULL,
    aggregate_type varchar(255),
    payload jsonb NOT NULL,
    published boolean NOT NULL default false
);