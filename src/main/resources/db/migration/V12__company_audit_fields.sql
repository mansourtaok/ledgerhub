ALTER TABLE companies RENAME COLUMN created_at TO created_date;
ALTER TABLE companies ADD COLUMN updated_date TIMESTAMP;
ALTER TABLE companies ADD COLUMN created_userid INT;
ALTER TABLE companies ADD COLUMN updated_userid INT;

ALTER TABLE companies
    ADD CONSTRAINT fk_companies_created_user
        FOREIGN KEY (created_userid)
        REFERENCES users(id)
        ON UPDATE CASCADE
        ON DELETE SET NULL;

ALTER TABLE companies
    ADD CONSTRAINT fk_companies_updated_user
        FOREIGN KEY (updated_userid)
        REFERENCES users(id)
        ON UPDATE CASCADE
        ON DELETE SET NULL;
