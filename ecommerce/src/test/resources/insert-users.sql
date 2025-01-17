CREATE EXTENSION pgcrypto;

INSERT INTO ecommerce.tb_user (full_name, login, password, expires_at, subject, fk_group_id)
VALUES('JUNIT USER', 'jun@jun.com', crypt('Junit@123', gen_salt('bf', 12)), current_timestamp + interval '2 years', '25a5afb1-c754-4038-9631-b04075480b5c', (SELECT id FROM ecommerce.tb_group WHERE group_acronym='ADMIN'));