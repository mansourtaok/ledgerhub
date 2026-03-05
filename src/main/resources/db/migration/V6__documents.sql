DROP TABLE IF EXISTS documents CASCADE;
CREATE TABLE documents (
    id SERIAL PRIMARY KEY,
    filename VARCHAR(100) NOT NULL,
    filepath VARCHAR(500) NOT NULL,
    reference_id BIGINT,

    CONSTRAINT fk_documents_reference_id
        FOREIGN KEY (reference_id)
        REFERENCES entities(id)
);