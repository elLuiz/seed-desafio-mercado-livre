-- Create default groups
INSERT INTO ecommerce.tb_group(group_name, group_acronym)
VALUES ('Administrator', 'ADMIN'), ('Consumer', 'CONSUMER');

-- Create default roles
INSERT INTO ecommerce.tb_role(role_description, role_acronym)
VALUES ('Allows the creation of admin users', 'CREATE_ADMIN'),
('Allows the deletion of accounts that violate the systems policies', 'DELETE_ACCOUNT'),
('Allows the deletion of fraudulent products or invalid ones', 'DELETE_PRODUCT'),
('Allows the deletion of inappropriate reviews', 'DELETE_REVIEW'),
('Allows the creation of categories', 'CREATE_CATEGORY'),
('Allows the creation of products and make them available', 'CREATE_PRODUCT'),
('Allows consumers to buy products', 'BUY_PRODUCT'),
('Allows the user to review a product', 'REVIEW_PRODUCT'),
('Allows the consumers to delete their reviews', 'DELETE_REVIEW');

-- Insert admin roles
INSERT INTO ecommerce.tb_group_role(fk_group_id, fk_role_id)
    SELECT tb_group.id, tb_role.id FROM tb_group, tb_role
    WHERE role_acronym IN ('CREATE_ADMIN', 'DELETE_ACCOUNT', 'DELETE_PRODUCT', 'DELETE_REVIEW', 'CREATE_CATEGORY') AND group_acronym='ADMIN';

-- Insert consumer roles
INSERT INTO ecommerce.tb_group_role(fk_group_id, fk_role_id)
    SELECT tb_group.id, tb_role.id FROM tb_group, tb_role
    WHERE role_acronym IN ('CREATE_PRODUCT', 'BUY_PRODUCT', 'CREATE_CATEGORY', 'REVIEW_PRODUCT', 'DELETE_REVIEW') AND group_acronym='CONSUMER';
