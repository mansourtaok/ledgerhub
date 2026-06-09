ALTER TABLE companies ADD COLUMN entity_id BIGINT REFERENCES entities(id);
ALTER TABLE companies ADD COLUMN website VARCHAR(255);
