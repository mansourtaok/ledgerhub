INSERT INTO system_lookup (category, code, name, description, is_active) VALUES
-- Major global currencies
('CURRENCY','USD','US Dollar','United States Dollar',true),
('CURRENCY','EUR','Euro','Eurozone common currency',true),
('CURRENCY','JPY','Japanese Yen','Currency of Japan',true),
('CURRENCY','GBP','British Pound','Pound Sterling of the United Kingdom',true),
('CURRENCY','CHF','Swiss Franc','Currency of Switzerland',true),
('CURRENCY','CAD','Canadian Dollar','Currency of Canada',true),
('CURRENCY','AUD','Australian Dollar','Currency of Australia',true),
('CURRENCY','CNY','Chinese Yuan','Renminbi – currency of China',true),
('CURRENCY','HKD','Hong Kong Dollar','Currency of Hong Kong',true),
('CURRENCY','SGD','Singapore Dollar','Currency of Singapore',true),

-- Middle East currencies
('CURRENCY','AED','UAE Dirham','United Arab Emirates Dirham',true),
('CURRENCY','SAR','Saudi Riyal','Currency of Saudi Arabia',true),
('CURRENCY','QAR','Qatari Riyal','Currency of Qatar',true),
('CURRENCY','KWD','Kuwaiti Dinar','Currency of Kuwait',true),
('CURRENCY','BHD','Bahraini Dinar','Currency of Bahrain',true),
('CURRENCY','OMR','Omani Rial','Currency of Oman',true),
('CURRENCY','JOD','Jordanian Dinar','Currency of Jordan',true),
('CURRENCY','LBP','Lebanese Lira','Currency of Lebanon',true),
('CURRENCY','ILS','Israeli New Shekel','Currency of Israel',true),

-- Asia-Pacific
('CURRENCY','INR','Indian Rupee','Currency of India',true),
('CURRENCY','KRW','South Korean Won','Currency of South Korea',true),
('CURRENCY','MYR','Malaysian Ringgit','Currency of Malaysia',true),
('CURRENCY','THB','Thai Baht','Currency of Thailand',true),
('CURRENCY','IDR','Indonesian Rupiah','Currency of Indonesia',true),
('CURRENCY','PHP','Philippine Peso','Currency of the Philippines',true),
('CURRENCY','PKR','Pakistani Rupee','Currency of Pakistan',true),
('CURRENCY','BDT','Bangladeshi Taka','Currency of Bangladesh',true),

-- Europe (non-Euro)
('CURRENCY','SEK','Swedish Krona','Currency of Sweden',true),
('CURRENCY','NOK','Norwegian Krone','Currency of Norway',true),
('CURRENCY','DKK','Danish Krone','Currency of Denmark',true),
('CURRENCY','PLN','Polish Zloty','Currency of Poland',true),
('CURRENCY','CZK','Czech Koruna','Currency of Czech Republic',true),
('CURRENCY','HUF','Hungarian Forint','Currency of Hungary',true),

-- Americas
('CURRENCY','MXN','Mexican Peso','Currency of Mexico',true),
('CURRENCY','BRL','Brazilian Real','Currency of Brazil',true),
('CURRENCY','ARS','Argentine Peso','Currency of Argentina',true),
('CURRENCY','CLP','Chilean Peso','Currency of Chile',true),
('CURRENCY','COP','Colombian Peso','Currency of Colombia',true),

-- Africa
('CURRENCY','ZAR','South African Rand','Currency of South Africa',true),
('CURRENCY','EGP','Egyptian Pound','Currency of Egypt',true),
('CURRENCY','NGN','Nigerian Naira','Currency of Nigeria',true),

('GENDER','M','Male','Male gender',true),
('GENDER','F','Female','Female gender',true),
('GENDER','U','Unspecified','Gender not specified',true),
('GENDER','O','Other','Other gender identity',true),

-- ADMIN ROLE
('ROLE','ADM','ADMIN','ADMIN',true),
('ROLE','MGR','MANAGER','Manager',true),
('ROLE','USR','USER','User',true),

-- JOB DESCRIPTION
('JOB_DESC','MGR','Manager','Manager',true),
('JOB_DESC','SR','Sales Representative','Sales Representative',true),
('JOB_DESC','STM','Store Manager','Store Manager',true),
('JOB_DESC','SK','Store Keeper','Store Keeper',true),
('JOB_DESC','AM','Accounting Manager','Accounting Manager',true),
('JOB_DESC','ACC','Accountant','Accountant',true),
('JOB_DESC','DRV','Driver','Driver',true),
('JOB_DESC','AST','Assistant','Assistant',true);


INSERT INTO countries (
    iso2,
    iso3,
    name,
    numeric_code,
    phone_code,
    currency_id
)
VALUES (
    'LB',
    'LBN',
    'Lebanon',
    '422',
    '+961',
    (
        SELECT id
        FROM system_lookup
        WHERE category = 'CURRENCY'
          AND code = 'LBP'
          AND is_active = true
        LIMIT 1
    )
);
