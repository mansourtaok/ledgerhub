CREATE TABLE users_roles (
    user_id INTEGER NOT NULL,
    role_id INTEGER NOT NULL,

    CONSTRAINT pk_users_roles PRIMARY KEY (user_id, role_id),

    CONSTRAINT fk_users_roles_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_users_roles_role
        FOREIGN KEY (role_id)
        REFERENCES system_lookup(id)
        ON DELETE RESTRICT
);

INSERT INTO users_roles (user_id, role_id)
SELECT id, role_id
FROM users;

ALTER TABLE users
DROP CONSTRAINT fk_users_role;

ALTER TABLE users
DROP COLUMN role_id;