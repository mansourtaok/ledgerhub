CREATE TABLE user_companies (
    id         SERIAL PRIMARY KEY,
    user_id    INT NOT NULL,
    company_id INT NOT NULL,
    is_default BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT NOW(),

    CONSTRAINT uq_user_company
        UNIQUE (user_id, company_id),

    CONSTRAINT fk_uc_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_uc_company
        FOREIGN KEY (company_id)
        REFERENCES companies(id)
        ON DELETE CASCADE
);

-- Enforces at most one default company per user at the DB level
CREATE UNIQUE INDEX uq_user_default_company
    ON user_companies (user_id)
    WHERE is_default = TRUE;
