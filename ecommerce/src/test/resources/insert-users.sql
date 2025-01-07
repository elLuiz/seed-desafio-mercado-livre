CREATE EXTENSION pgcrypto;

INSERT INTO ecommerce.tb_user (full_name, login, password, expires_at, subject, fk_group_id)
VALUES('JUNIT USER', 'jun@jun.com', crypt('Junit@123', gen_salt('bf', 12)), current_timestamp + interval '2 years',gen_random_uuid(), (SELECT id FROM ecommerce.tb_group WHERE group_acronym='ADMIN'));