INSERT INTO t_user(id, username, password)
VALUES (1, 'zhang', '123456'),
       (2, 'li', '123456');
INSERT INTO t_role(id, role_name)
VALUES (1, 'admin'),
       (2, 'common');
INSERT INTO t_permission(id, permission_code)
VALUES (1, 'user:add'),
       (2, 'user:delete'),
       (3, 'user:query');

-- zhang 是 admin，拥有全部权限
INSERT INTO t_user_role(user_id, role_id)
VALUES (1, 1);
INSERT INTO t_role_permission(role_id, permission_id)
VALUES (1, 1),
       (1, 2),
       (1, 3);

-- li 是 common，只有 user:add
INSERT INTO t_user_role(user_id, role_id)
VALUES (2, 2);
INSERT INTO t_role_permission(role_id, permission_id)
VALUES (2, 1);