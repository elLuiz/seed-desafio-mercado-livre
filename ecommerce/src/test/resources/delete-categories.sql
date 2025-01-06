DELETE FROM ecommerce.tb_category WHERE fk_category_id IS NOT NULL;
DELETE FROM ecommerce.tb_category WHERE fk_category_id IS NULL;