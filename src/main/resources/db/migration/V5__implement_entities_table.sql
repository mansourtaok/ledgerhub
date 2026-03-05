CREATE TABLE entities (
    id BIGSERIAL PRIMARY KEY,
    entity_type VARCHAR(50) NOT NULL
);
ALTER TABLE purchase_items DROP CONSTRAINT IF EXISTS purchase_items_item_id_fkey;
ALTER TABLE stock_movements DROP CONSTRAINT IF EXISTS fk_stock_movements_item_id;
ALTER TABLE invoice_items DROP CONSTRAINT IF EXISTS fk_invoice_items_item_id;

ALTER TABLE items ALTER COLUMN id DROP DEFAULT;
ALTER TABLE items DROP CONSTRAINT items_pkey;
ALTER TABLE items ALTER COLUMN id TYPE BIGINT;
ALTER TABLE items ADD CONSTRAINT fk_items_entities FOREIGN KEY (id) REFERENCES entities(id);
ALTER TABLE items ADD CONSTRAINT items_pkey PRIMARY KEY (id);
--

ALTER TABLE purchase_items ALTER COLUMN item_id TYPE BIGINT;
ALTER TABLE purchase_items ADD CONSTRAINT purchase_items_item_id_fkey FOREIGN KEY (item_id) REFERENCES items(id);
--

ALTER TABLE stock_movements ALTER COLUMN item_id TYPE BIGINT;
ALTER TABLE stock_movements ADD CONSTRAINT fk_stock_movements_item_id FOREIGN KEY (item_id) REFERENCES items(id);
--

ALTER TABLE invoice_items ALTER COLUMN item_id TYPE BIGINT;
ALTER TABLE invoice_items ADD CONSTRAINT fk_invoice_items_item_id FOREIGN KEY (item_id) REFERENCES items(id);
