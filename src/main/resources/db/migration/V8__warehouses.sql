CREATE TABLE warehouses (
    id SERIAL PRIMARY KEY,
    company_id INT NOT NULL,
    name VARCHAR(150) NOT NULL,
    main_contact VARCHAR(150) NOT NULL,
    email        VARCHAR(150),
    contact_number      VARCHAR(50),
    address      TEXT,    
    active       BOOLEAN DEFAULT TRUE,
    created_on TIMESTAMP DEFAULT NOW(),
    
    CONSTRAINT FK_warehouses_company_id
        FOREIGN KEY (company_id)
        REFERENCES companies(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);