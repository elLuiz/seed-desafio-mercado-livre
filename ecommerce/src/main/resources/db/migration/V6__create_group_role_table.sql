CREATE TABLE IF NOT EXISTS ecommerce.tb_group_role (
    fk_group_id bigint NOT NULL,
    fk_role_id bigint NOT NULL,

    constraint pkGroupRole PRIMARY KEY(fk_group_id, fk_role_id),
    constraint fkGroupId FOREIGN KEY(fk_group_id)
        REFERENCES ecommerce.tb_group(id),
    constraint fkRoleId FOREIGN KEY(fk_role_id)
        REFERENCES ecommerce.tb_role(id)
);