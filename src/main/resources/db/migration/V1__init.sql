CREATE TABLE system_lookup (
    id SERIAL PRIMARY KEY,
    category VARCHAR(50) NOT NULL,
    code VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    CONSTRAINT uniq_category_code UNIQUE (category, code)
);



CREATE TABLE countries (
    id SERIAL PRIMARY KEY,
    iso2 CHAR(2) NOT NULL UNIQUE,
    iso3 CHAR(3) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    numeric_code CHAR(3),
    phone_code VARCHAR(10),
    currency_id INT,
    
     CONSTRAINT fk_countries_currency_id
        FOREIGN KEY (currency_id)
        REFERENCES system_lookup(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE users (
    id          SERIAL PRIMARY KEY,
    email            VARCHAR(150) NOT NULL UNIQUE,
    password_hash    VARCHAR(255) NOT NULL,
    abbreviation     VARCHAR(3),
    preferred_lang   INTEGER,    
    preferred_color  VARCHAR(7),
    role_id          INTEGER NOT NULL,
    created_at       TIMESTAMP DEFAULT NOW(),
    active           BOOLEAN DEFAULT TRUE,

    -- Foreign keys
    CONSTRAINT fk_users_role
        FOREIGN KEY (role_id)
        REFERENCES system_lookup(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_users_lang
        FOREIGN KEY (preferred_lang)
        REFERENCES system_lookup(id)
        ON UPDATE CASCADE
        ON DELETE SET NULL
);

CREATE TABLE companies (
    id   SERIAL PRIMARY KEY,
    name         VARCHAR(150) NOT NULL,
    email        VARCHAR(150),
    phone        VARCHAR(50),
    address      TEXT,
    tax_number   VARCHAR(50),
    header       VARCHAR(200),
    footer       VARCHAR(200),
    country_id   INT,    
    created_at   TIMESTAMP DEFAULT NOW(),
    active       BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_companies_country_id
        FOREIGN KEY (country_id)
        REFERENCES countries(id)
        ON UPDATE CASCADE
        ON DELETE SET NULL
);

CREATE TABLE banks (
    id SERIAL PRIMARY KEY,
    company_id INT NOT NULL,
    bank_name VARCHAR(150) NOT NULL,
    bank_acc_nbr VARCHAR(50),
    bank_acc_label VARCHAR(150),
    fin_acc_nbr VARCHAR(50),
    swift_nbr VARCHAR(50),
    iban_nbr VARCHAR(50) UNIQUE,
    currency_id INT NOT NULL,
    country_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),

    CONSTRAINT fk_banks_company_id
        FOREIGN KEY (company_id)
        REFERENCES companies(id)
        ON UPDATE CASCADE
        ON DELETE SET NULL,

    CONSTRAINT fk_banks_currency_id
        FOREIGN KEY (currency_id)
        REFERENCES system_lookup(id),

    CONSTRAINT fk_banks_country_id
        FOREIGN KEY (country_id)
        REFERENCES countries(id)
);

CREATE TABLE staffs (
    id            SERIAL PRIMARY KEY,
    company_id          INT,
    full_name           VARCHAR(150) NOT NULL,
    gender_id           INT,
    address             TEXT,
    contact_number      VARCHAR(50),
    email               VARCHAR(150),

    job_description     INT NOT NULL,
    join_date           DATE,
    leave_date          DATE,

    have_cnss           BOOLEAN DEFAULT FALSE,
    cnss_number         VARCHAR(50),

    notes               TEXT,

    created_at          TIMESTAMP DEFAULT NOW(),
    created_userid      INT,
    last_update_at      TIMESTAMP,
    last_update_userid  INT,

    active              BOOLEAN DEFAULT TRUE,

    -- FOREIGN KEY CONSTRAINTS
    CONSTRAINT fk_staffs_gender_id
        FOREIGN KEY (gender_id)
        REFERENCES system_lookup(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
		
    CONSTRAINT fk_staffs_company_id
        FOREIGN KEY (company_id)
        REFERENCES companies(id)
        ON UPDATE CASCADE
        ON DELETE SET NULL,

    CONSTRAINT fk_staffs_created_user
        FOREIGN KEY (created_userid)
        REFERENCES users(id)
        ON UPDATE CASCADE
        ON DELETE SET NULL,

    CONSTRAINT fk_staff_last_update_user
        FOREIGN KEY (last_update_userid)
        REFERENCES users(id)
        ON UPDATE CASCADE
        ON DELETE SET NULL
);


CREATE TABLE profiles (
    id           SERIAL PRIMARY KEY,

    company_id           INT,
    category_id             INT,
    type_id                 INT,

    name                 VARCHAR(150) NOT NULL,
    default_currency     INT,
    payment_term         INT,
    country_id              INT,
    address              TEXT,
    contact_number       VARCHAR(50),
    fax_number           VARCHAR(50),
    vendor_account       VARCHAR(50),
    customer_account     VARCHAR(50),
    tax_number           VARCHAR(50),
    notes                TEXT,

    created_at           TIMESTAMP DEFAULT NOW(),
    created_userid       INT,

    last_update_at       TIMESTAMP,
    last_update_userid   INT,

    CONSTRAINT fk_profiles_category_id
        FOREIGN KEY (category_id)
        REFERENCES system_lookup(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
		
    CONSTRAINT fk_profiles_type_id
        FOREIGN KEY (type_id)
        REFERENCES system_lookup(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_profiles_company_id
        FOREIGN KEY (company_id)
        REFERENCES companies(id)
        ON UPDATE CASCADE
        ON DELETE SET NULL		,
		
    CONSTRAINT fk_profiles_country_id
        FOREIGN KEY (country_id)
        REFERENCES companies(id)
        ON UPDATE CASCADE
        ON DELETE SET NULL		,		
		

    CONSTRAINT fk_profiles_created_user
        FOREIGN KEY (created_userid)
        REFERENCES users(id)
		ON UPDATE CASCADE
        ON DELETE SET NULL,

    CONSTRAINT fk_profiles_last_update_user
        FOREIGN KEY (last_update_userid)
        REFERENCES users(id)
        ON UPDATE CASCADE
        ON DELETE SET NULL		
);


CREATE TABLE persons (
    id SERIAL PRIMARY KEY,

    profile_id INT,
    company_id INT,
    name VARCHAR(150),
    job_description_id INT,

    email VARCHAR(150) NOT NULL,
    contact_number VARCHAR(50),
    extension VARCHAR(10),

    created_at TIMESTAMP DEFAULT NOW(),
    created_userid INT,

    last_update_at TIMESTAMP,
    last_update_userid INT,

    CONSTRAINT UQ_profile_persons_email
        UNIQUE (email),

    CONSTRAINT FK_persons_profile_id
        FOREIGN KEY (profile_id)
        REFERENCES profiles(id)
        ON UPDATE CASCADE
        ON DELETE SET NULL,

    CONSTRAINT FK_profiles_company_id
        FOREIGN KEY (company_id)
        REFERENCES companies(id)
        ON UPDATE CASCADE
        ON DELETE SET NULL,

    CONSTRAINT FK_profiles_created_user
        FOREIGN KEY (created_userid)
        REFERENCES users(id)
        ON UPDATE CASCADE
        ON DELETE SET NULL,
		

    CONSTRAINT FK_profiles_last_update_user
        FOREIGN KEY (last_update_userid)
        REFERENCES users(id)
        ON UPDATE CASCADE
        ON DELETE SET NULL
		
);

CREATE TABLE item_categories (
    id SERIAL PRIMARY KEY,    
    company_id INT NOT NULL,    
    label VARCHAR(150) NOT NULL,    
    parent_id INT DEFAULT 0,    
    created_at TIMESTAMP DEFAULT NOW(),    
    created_userid INT,    
    last_update_at TIMESTAMP,    
    last_update_userid INT,


    CONSTRAINT FK_item_categories_company_id
        FOREIGN KEY (company_id)
        REFERENCES companies(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,		

    CONSTRAINT FK_item_categories_created_user
        FOREIGN KEY (created_userid)
        REFERENCES users(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,		

    CONSTRAINT FK_item_categories_last_update_user
        FOREIGN KEY (last_update_userid)
        REFERENCES users(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT		
);

CREATE TABLE items (
    id SERIAL PRIMARY KEY,
    company_id INT NOT NULL,
    category_id INT,
    currency_id INT,
    name VARCHAR(150) NOT NULL,
    sku VARCHAR(50) NOT NULL,
    description TEXT,
    unit VARCHAR(3),
    returnable_stock BOOLEAN,
    stock_qty_notify NUMERIC(12,3),
    dimension VARCHAR(50),
    dimension_measure VARCHAR(2),
    weight NUMERIC(12,3),
    packaging VARCHAR(2),
    manufacturer VARCHAR(150),
    brand VARCHAR(150),
    cost_price NUMERIC(12,3) NOT NULL,
    purchase_account VARCHAR(50),
    selling_price NUMERIC(12,3) NOT NULL,
    selling_account VARCHAR(50),
    notes TEXT,
    taxable BOOLEAN,
    tax_percent NUMERIC(5,2),
    stock_quantity NUMERIC(12,3) DEFAULT 0,
    have_expiry_date BOOLEAN,
    lot_number VARCHAR(150),
    expiry_date DATE,

    online_sale BOOLEAN DEFAULT TRUE,
    status BOOLEAN DEFAULT TRUE,

    created_at TIMESTAMP DEFAULT NOW(),
    created_userid INT,

    last_update_at TIMESTAMP,
    last_update_userid INT,

    CONSTRAINT UQ_items_sku
        UNIQUE (sku),

    CONSTRAINT FK_items_company_id
        FOREIGN KEY (company_id)
        REFERENCES companies(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,		

    CONSTRAINT FK_items_category_id
        FOREIGN KEY (category_id)
        REFERENCES item_categories(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,		

    CONSTRAINT FK_items_currency_id
        FOREIGN KEY (currency_id)
        REFERENCES system_lookup(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,		

    CONSTRAINT FK_items_created_user
        FOREIGN KEY (created_userid)
        REFERENCES users(id)
		ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT FK_items_last_update_user
        FOREIGN KEY (last_update_userid)
        REFERENCES users(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT		
);

CREATE TABLE documents (
    id        SERIAL PRIMARY KEY,
    company_id         INT,
    type               VARCHAR(1) NOT NULL,
    reference_id       INT NOT NULL,
    document_type      TEXT,
    document_filename  VARCHAR(100),
    document_filepath  VARCHAR(500),
    created_at         TIMESTAMP DEFAULT NOW(),
    created_userid     INT,
	
        CONSTRAINT fk_documents_company_id
		FOREIGN KEY (company_id)
        REFERENCES companies(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,		
    
		CONSTRAINT fk_documents_reference_id
        FOREIGN KEY (reference_id)
        REFERENCES system_lookup(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,	
	
        CONSTRAINT fk_documents_created_userid
		FOREIGN KEY (created_userid)
        REFERENCES users(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT		
);


CREATE TABLE purchases (
    id             SERIAL PRIMARY KEY,
    company_id              INT ,
    vendor_id               INT,
    order_number            VARCHAR(50),
    bill_number             VARCHAR(50),
    purchase_date           DATE DEFAULT NOW(),
    currency                VARCHAR(3),
    payment_method          VARCHAR(2),
    due_date                DATE,
    total_amount            NUMERIC(12,3) DEFAULT 0,
    tax_amount              NUMERIC(12,3) DEFAULT 0,
    discount_amount         NUMERIC(12,3) DEFAULT 0,
    grand_total             NUMERIC(12,3) DEFAULT 0,
    shipment_type           INT,
    expected_reception_date DATE,
    address_delivery        TEXT,
    notes                   TEXT,
    terms_and_conditions    TEXT,
    status                  INT,
    created_at              TIMESTAMP DEFAULT NOW(),
    created_userid          INT,	
    last_update_at          TIMESTAMP,
    last_update_userid      INT,
	
    CONSTRAINT fk_purchases_company_id
	FOREIGN KEY (company_id)
    REFERENCES companies(id)
	ON UPDATE CASCADE
    ON DELETE RESTRICT,	

	CONSTRAINT fk_documents_status
    FOREIGN KEY (status)
    REFERENCES system_lookup(id)
    ON UPDATE CASCADE
    ON DELETE RESTRICT,

    CONSTRAINT fk_purchases_vendor_id
	FOREIGN KEY (vendor_id)
    REFERENCES profiles(id)
	ON UPDATE CASCADE
    ON DELETE RESTRICT,		
	
    CONSTRAINT fk_purchases_created_user
    FOREIGN KEY (created_userid)
    REFERENCES users(id)
	ON UPDATE CASCADE
    ON DELETE RESTRICT,	
	
    CONSTRAINT fk_purchases_last_update_user
    FOREIGN KEY (last_update_userid)
    REFERENCES users(id)
	ON UPDATE CASCADE
    ON DELETE RESTRICT
	
);

CREATE TABLE purchase_items(
    id             SERIAL PRIMARY KEY,
	purchase_id             INT,  
	company_id              INT ,
	item_id                 INT,
    item_acc_number         VARCHAR(50),
    quantity                NUMERIC(12,3) not null,	
	unit_cost               NUMERIC(12,3) not null,
	tax_rate                NUMERIC(5,2),	
	total               NUMERIC(12,3),
	created_at              TIMESTAMP DEFAULT NOW(),
	
	
    CONSTRAINT fk_purchases_items_purchase_id
	FOREIGN KEY (purchase_id)
    REFERENCES purchases(id)
	ON UPDATE CASCADE
    ON DELETE RESTRICT,
	
    CONSTRAINT fk_purchases_items_company_id
	FOREIGN KEY (company_id)
    REFERENCES companies(id)
	ON UPDATE CASCADE
    ON DELETE RESTRICT
	
	
);

CREATE TABLE stock_movements (
    id SERIAL PRIMARY KEY,
    company_id INT NOT NULL,
    item_id INT NOT NULL,
    movement_type INT not null,
    reference_type INT not null,
    reference_id VARCHAR(50),
    quantity NUMERIC(12,3) NOT NULL,
    reason TEXT,
    created_at TIMESTAMP DEFAULT NOW(),
    created_userid INT,
    last_update_at TIMESTAMP,
    last_update_userid INT,
    
    CONSTRAINT fk_stock_movements_id
        FOREIGN KEY (company_id)
        REFERENCES companies(id)
		ON UPDATE CASCADE
		ON DELETE RESTRICT,			
        
    CONSTRAINT fk_stock_movements_item_id
        FOREIGN KEY (item_id)
        REFERENCES items(id)
		ON UPDATE CASCADE
		ON DELETE RESTRICT,			
        
    CONSTRAINT fk_stock_movements_created_user
        FOREIGN KEY (created_userid)
        REFERENCES users(id)
	ON UPDATE CASCADE
    ON DELETE RESTRICT,			
        
    CONSTRAINT fk_stock_movements_last_update_user
        FOREIGN KEY (last_update_userid)
        REFERENCES users(id)
	ON UPDATE CASCADE
    ON DELETE RESTRICT,

    CONSTRAINT fk_stock_movements_movement_type
        FOREIGN KEY (movement_type)
        REFERENCES system_lookup(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,	
		
    CONSTRAINT fk_stock_movements_reference_type
        FOREIGN KEY (reference_type)
        REFERENCES system_lookup(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT		
);


CREATE TABLE invoices (

    id SERIAL PRIMARY KEY,
    company_id INT NOT NULL,
    customer_id INT NOT NULL,
    order_number VARCHAR(50),
    invoice_number VARCHAR(50),
    currency VARCHAR(3),
    issue_date DATE DEFAULT NOW(),
    due_date DATE,
    payment_term VARCHAR(2),
    total_amount NUMERIC(12,3) DEFAULT 0,
    tax_amount NUMERIC(12,3) DEFAULT 0,
    discount_amount NUMERIC(12,3) DEFAULT 0,
    shipment_charges NUMERIC(12,3) DEFAULT 0,
    grand_total NUMERIC(12,3) DEFAULT 0,
    shipment_type INT,
    expected_reception_date DATE,
    notes TEXT,
    terms_and_conditions TEXT,
    staff_id INT,
    invoice_status INT,
    created_at TIMESTAMP DEFAULT NOW(),
    created_userid INT,
    last_update_at TIMESTAMP,
    last_update_userid INT,

    CONSTRAINT fk_invoices_company_id
        FOREIGN KEY (company_id)
        REFERENCES companies(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_invoices_customer_id
        FOREIGN KEY (customer_id)
        REFERENCES profiles(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_invoices_staff
        FOREIGN KEY(staff_id)
        REFERENCES staffs(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_invoices_created_user_id
        FOREIGN KEY (created_userid)
        REFERENCES users(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_invoices_last_update_user_id
        FOREIGN KEY (last_update_userid)
        REFERENCES users(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);


CREATE TABLE invoice_items (

    id SERIAL PRIMARY KEY,
    invoice_id INT NOT NULL,
    company_id INT NOT NULL,
    item_id INT NOT NULL,
    item_acc_number VARCHAR(50),
    quantity NUMERIC(12,3) NOT NULL,
    unit_price NUMERIC(12,3) NOT NULL,
    discount NUMERIC(12,3) DEFAULT 0,
    tax_rate NUMERIC(5,2) DEFAULT 0,
    total NUMERIC(12,3) DEFAULT 0,
    created_at TIMESTAMP DEFAULT NOW(),

    CONSTRAINT fk_invoice_items_invoice_id
        FOREIGN KEY (invoice_id)
        REFERENCES invoices(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_invoice_items_company_id
        FOREIGN KEY (company_id)
        REFERENCES companies(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_invoice_items_item_id
        FOREIGN KEY (item_id)
        REFERENCES items(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT		
);


CREATE TABLE payments (
    id SERIAL PRIMARY KEY,
    company_id INT NOT NULL,
    type VARCHAR(1),
    reference_id VARCHAR(50),
    date DATE NOT NULL,
    payment_method INT NOT NULL,
    amount NUMERIC(12,3) NOT NULL,
    notes TEXT DEFAULT '0',
    created_at TIMESTAMP DEFAULT NOW(),
    created_userid INT,
    last_update_at TIMESTAMP,
    last_update_userid INT,

    CONSTRAINT fk_payments_company_id
        FOREIGN KEY (company_id)
        REFERENCES companies(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
		

    CONSTRAINT fk_payments_created_userid
        FOREIGN KEY (created_userid)
        REFERENCES users(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,		
		

    CONSTRAINT fk_payments_last_update_userid
        FOREIGN KEY (last_update_userid)
        REFERENCES users(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
		
    CONSTRAINT fk_payments_payment_method
        FOREIGN KEY (payment_method)
        REFERENCES system_lookup(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT		
);

CREATE TABLE expense_types (
    id SERIAL PRIMARY KEY,
    company_id INT NOT NULL,
    exp_type_label VARCHAR(150),

    CONSTRAINT fk_company
        FOREIGN KEY (company_id)
        REFERENCES companies(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT			
);


CREATE TABLE expenses (
    id SERIAL PRIMARY KEY,
    exp_type_id INT NOT NULL,
    company_id INT NOT NULL,
    date VARCHAR(150),
    amount NUMERIC(12,3),
    currency VARCHAR(3),
    payment_method VARCHAR(2),
    notes TEXT,

    CONSTRAINT fk_expenses_expense_type_id
        FOREIGN KEY (exp_type_id)
        REFERENCES expense_types(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
		

    CONSTRAINT fk_expenses_company_id
        FOREIGN KEY (company_id)
        REFERENCES companies(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT			
		
);

CREATE TABLE accounts (
    id SERIAL PRIMARY KEY,
    company_id INT NOT NULL,
    acc_number VARCHAR(20),
    acc_label VARCHAR(150),
    acc_type INT,
    type INT,
    mg_id VARCHAR(4),
    anal_code VARCHAR(3),

    CONSTRAINT fk_accounts_company_id
        FOREIGN KEY (company_id)
        REFERENCES companies(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
		
    CONSTRAINT fk_accounts_acc_type
        FOREIGN KEY (acc_type)
        REFERENCES system_lookup(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_accounts_type
        FOREIGN KEY (type)
        REFERENCES system_lookup(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT		
		
);