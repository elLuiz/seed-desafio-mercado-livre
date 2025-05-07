ALTER TABLE ecommerce.tb_order ADD COLUMN IF NOT EXISTS order_processing_transaction_id VARCHAR(255);
ALTER TABLE ecommerce.tb_order ADD COLUMN IF NOT EXISTS order_processed_at TIMESTAMP WITH TIME ZONE;
ALTER TABLE ecommerce.tb_order ADD CONSTRAINT uk_tb_order_order_processing_transaction_id UNIQUE(order_processing_transaction_id);