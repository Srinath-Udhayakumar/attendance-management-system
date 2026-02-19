-- V3__add_employee_id_column.sql
-- Add employee_id column to users table

-- Add employee_id column if it doesn't exist
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE column_name = 'employee_id' 
        AND table_name = 'users' 
        AND table_schema = 'public'
    ) THEN
        ALTER TABLE users ADD COLUMN employee_id VARCHAR(20) UNIQUE;
    END IF;
END $$;
